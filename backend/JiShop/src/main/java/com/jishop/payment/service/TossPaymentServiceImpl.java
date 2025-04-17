package com.jishop.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.config.TossPaymentConfig;
import com.jishop.order.domain.Order;
import com.jishop.payment.domain.*;
import com.jishop.payment.dto.TossConfirmRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements TossPaymentService {

    private final RestTemplate restTemplate;
    private final TossPaymentConfig tossPaymentConfig;
    private final ObjectMapper objectMapper;

    private static final Map<String, PaymentMethod> PAYMENT_METHOD_MAP = Map.of(
            "카드", PaymentMethod.CARD,
            "간편결제", PaymentMethod.EASY_PAY
    );

    private static final Map<String, PaymentProvider> PAYMENT_PROVIDER_MAP = Map.of(
            "카카오페이", PaymentProvider.KAKAO_PAY,
            "토스페이", PaymentProvider.TOSS_PAY,
            "네이버페이", PaymentProvider.NAVER_PAY
    );

    /**
     * 토스 결제 승인 API 호출 후 Payment 객체를 반환하는 메서드
     *
     * @param request   토스 결제 승인 요청 DTO
     * @return Payment 객체
     */
    @Override
    public Payment confirmPayment(TossConfirmRequest request, Order order) {

        // 결제 승인 요청 데이터 생성
        String jsonBody = serializeRequest(request);

        // 요청 헤더 설정 - 시크릿 키 인코딩
        HttpHeaders headers = tossPaymentConfig.getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);

        // 토스 결제 승인 API 호출
        ResponseEntity<String> responseEntity = sendConfirmRequest(httpEntity);

        // 토스 응답 파싱
        JsonNode paymentNode = parseResponse(responseEntity.getBody());

        // Payment 엔티티 생성 및 반환
        return createPayment(paymentNode, order);
    }

    /**
     * 결제 승인 요청 DTO를 JSON으로 직렬화
     *
     * @param request   토스 결제 승인 요청 DTO
     * @return 직렬화된 JSON 문자열
     */
    private String serializeRequest(TossConfirmRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.CONFIRM_REQUEST_SERIALIZATION_FAILED);
        }
    }

    /**
     * 토스 결제 승인 API 호출
     *
     * @param httpEntity 시크릿 키 인코딩 헤더 + 요청 바디
     * @return API 응답 결과
     */
    private ResponseEntity<String> sendConfirmRequest(HttpEntity<String> httpEntity) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    tossPaymentConfig.getConfirmUrl(),
                    httpEntity,
                    String.class);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new DomainException(ErrorType.TOSS_CONFIRM_FAILED);
            }
            return responseEntity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.TOSS_CONFIRM_REQEUST_FAILED);
        }
    }

    /**
     * 토스 결제 승인 응답을 파싱하는 메서드
     *
     * @param responseBody 토스 응답 본문
     * @return 파싱된 JsonNode 객체
     */
    private JsonNode parseResponse(String responseBody) {
        try {
            String convertedBody = convertToUTF8(responseBody);
            return objectMapper.readTree(convertedBody);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DomainException(ErrorType.TOSS_RESPONSE_PARSING_FAILED);
        }
    }

    /**
     * JsonNode에서 결제정보를 추출해 Payment 객체 샏성 메서드
     * 
     * @param paymentNode  응답 루트 JsonNode
     * @return Payment 객체
     */
    private Payment createPayment(JsonNode paymentNode, Order order) {
        String paymentKey = paymentNode.get("paymentKey").asText();
        String orderNumber = paymentNode.get("orderId").asText();
        PaymentStatus status = PaymentStatus.valueOf(paymentNode.get("status").asText());

        LocalDateTime requestedAt = java.time.OffsetDateTime.parse(paymentNode.get("requestedAt").asText()).toLocalDateTime();
        LocalDateTime approvedAt = java.time.OffsetDateTime.parse(paymentNode.get("approvedAt").asText()).toLocalDateTime();

        String methodText = paymentNode.get("method").asText().trim();
        PaymentMethod method = convertToPaymentMethod(methodText);

        String currency = paymentNode.get("currency").asText();
        int totalAmount = paymentNode.get("totalAmount").asInt();
        int suppliedAmount = getIntOrDefault(paymentNode, "suppliedAmount", 0);
        int vat = getIntOrDefault(paymentNode, "vat", 0);
        int taxFreeAmount = getIntOrDefault(paymentNode, "taxFreeAmount", 0);

        //  카드 정보(card), 간편 결제 정보(easyPay) 파싱
        CardInfo cardInfo = parseCardInfo(paymentNode);
        EasyPayInfo easyPayInfo = parseEasyPayInfo(paymentNode);

        // Payment 엔티티 생성 및 반환
        return Payment.builder()
                .paymentKey(paymentKey)
                .order(order)
                .orderNumber(orderNumber)
                .status(status)
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .method(method)
                .currency(currency)
                .totalAmount(totalAmount)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .taxFreeAmount(taxFreeAmount)
                .card(cardInfo)
                .easyPay(easyPayInfo)
                .build();
    }

    /**
     * 응답에서 카드 결제 정보를 추출해 CardInfo 객체 생성
     * 
     * @param paymentNode
     * @return CardInfo(카드 결제) 객체, 다른 결제수단으로 결제 시 null
     */
    private CardInfo parseCardInfo(JsonNode paymentNode) {
        if (!paymentNode.has("card") || paymentNode.get("card").isNull()) {
            return null;
        }

        try {
            JsonNode cardNode = paymentNode.get("card");

            return CardInfo.builder()
                    .issuerCode(getTextOrNull(cardNode, "issuerCode"))
                    .acquirerCode(getTextOrNull(cardNode, "acquirerCode"))
                    .number(getTextOrNull(cardNode, "number"))
                    .installmentPlanMonths(getIntOrDefault(cardNode, "installmentPlanMonths", 0))
                    .isInterestFree(getBooleanOrDefault(cardNode, "isInterestFree", false))
                    .approveNo(getTextOrNull(cardNode, "approveNo"))
                    .useCardPoint(getBooleanOrDefault(cardNode, "useCardPoint", false))
                    .cardType(getTextOrNull(cardNode, "cardType"))
                    .ownerType(getTextOrNull(cardNode, "ownerType"))
                    .acquireStatus(getTextOrNull(cardNode, "acquireStatus"))
                    .amount(getIntOrDefault(cardNode, "amount", 0))
                    .build();
        } catch (Exception e) {
            log.error("Failed to parse Card info: {}", e.getMessage(), e);
            return null;
        }
    }


    /**
     * 응답에서 간편 결제 정보를 추출해 CardInfo 객체 생성
     * 
     * @param paymentNode
     * @return EasyPay(간편 결제)객체, 다른 결제수단으로 결제 시 null
     */
    private EasyPayInfo parseEasyPayInfo(JsonNode paymentNode) {
        if (!paymentNode.has("easyPay") || paymentNode.get("easyPay").isNull()) {
            return null;
        }

        try {
            JsonNode easyPayNode = paymentNode.get("easyPay");
            String providerText = getTextOrNull(easyPayNode, "provider");

            if (providerText == null) {
                log.warn("Provider field is missing in easyPay node");
                return null;
            }

            PaymentProvider provider = convertToPaymentProvider(providerText);
            int amount = getIntOrDefault(easyPayNode, "amount", 0);
            int discountAmount = getIntOrDefault(easyPayNode, "discountAmount", 0);

            return EasyPayInfo.builder()
                    .provider(provider)
                    .amount(amount)
                    .discountAmount(discountAmount)
                    .build();
        } catch (Exception e) {
            log.error("Failed to parse EasyPay info: {}", e.getMessage(), e);
            return null;
        }
    }

    private String convertToUTF8(String value) {
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    private PaymentProvider convertToPaymentProvider(String providerText) {
        return Optional.ofNullable(PAYMENT_PROVIDER_MAP.get(providerText))
                .orElseThrow(() -> new DomainException(ErrorType.TOSS_RESPONSE_PARSING_FAILED));
    }

    private PaymentMethod convertToPaymentMethod(String methodText) {
        return Optional.ofNullable(PAYMENT_METHOD_MAP.get(methodText))
                .orElseThrow(() -> new DomainException(ErrorType.TOSS_RESPONSE_PARSING_FAILED));
    }

    private String getTextOrNull(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() ?
                node.get(fieldName).asText() : null;
    }

    private int getIntOrDefault(JsonNode node, String fieldName, int defaultValue) {
        return node.has(fieldName) && !node.get(fieldName).isNull() ?
                node.get(fieldName).asInt() : defaultValue;
    }

    private boolean getBooleanOrDefault(JsonNode node, String fieldName, boolean defaultValue) {
        return node.has(fieldName) && !node.get(fieldName).isNull() ?
                node.get(fieldName).asBoolean() : defaultValue;
    }
}

