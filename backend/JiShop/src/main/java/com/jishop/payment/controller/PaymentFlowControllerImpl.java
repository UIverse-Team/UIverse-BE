package com.jishop.payment.controller;

import com.jishop.config.TossPaymentConfig;
import com.jishop.order.service.OrderService;
import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentFlowControllerImpl implements PaymentFlowController {

    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;

    /**
     * 결제창 띄우기 - 타임리프 기반
     * 주문 생성 후 프론트에서 주문 정보를 백엔드로 넘겨줘야 함!
     */
    @GetMapping("/checkout")
    public String checkoutPage(@RequestParam String orderNumber,
                               @RequestParam int amount,
                               @RequestParam String orderName,
                               Model model) {
        model.addAttribute("clientKey", tossPaymentConfig.getClientKey());
        model.addAttribute("orderId", orderNumber);
        model.addAttribute("amount", amount);
        model.addAttribute("originalAmount", amount);
        model.addAttribute("orderName", orderName);
        model.addAttribute("customerKey", orderNumber);

        return "checkout";
    }

    /**
     * Toss 결제 성공 시 결제 승인 처리
     */
    @GetMapping("/success")
    public String handleSuccess(@RequestParam String paymentKey,
                                @RequestParam String orderId,
                                @RequestParam int amount,
                                RedirectAttributes redirectAttributes) {
        try {
            PaymentConfirmRequest confirmRequest = new PaymentConfirmRequest(paymentKey, orderId, amount);
            PaymentConfirmResponse response = paymentService.confirmPayment(confirmRequest);
            redirectAttributes.addFlashAttribute("message", response.message());
        } catch (Exception e) {
            log.error("결제 승인 실패", e);
            redirectAttributes.addFlashAttribute("message", "결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/payments/result";
    }

    /**
     * 결제 실패 시 화면 표시
     */
    @GetMapping("/fail")
    public String handleFail(@RequestParam(required = false) String code,
                             @RequestParam(required = false) String message,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "결제 실패: " + message);
        return "redirect:/payment/result";
    }

    /**
     * 결제 결과 화면 (성공 or 실패)
     */
    @GetMapping("/result")
    public String resultPage() {
        return "paymentResult";
    }
}

