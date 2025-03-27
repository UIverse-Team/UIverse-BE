package com.jishop.question.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.repository.OrderRepository;
import com.jishop.question.domain.Question;
import com.jishop.question.domain.QuestionCategory;
import com.jishop.question.dto.QuestionRequest;
import com.jishop.question.dto.QuestionResponse;
import com.jishop.question.repository.QuestionCategoryRepository;
import com.jishop.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final OrderRepository orderRepository;

    /**
     * 문의사항 작성에 필요한 데이터를 검증하고, Question 엔티티를 생성하는 메서드
     * 
     * @param request   문의사항 작성 DTO
     * @param user      사용자 정보
     * @return 생성된 Question id 
     */
    @Override
    public Long createQuestion(QuestionRequest request, User user) {

        // 카테고리 검증
        QuestionCategory category = questionCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new DomainException(ErrorType.MATCH_NOT_USER));
    
        // 주문번호가 필수인 문의유형의 주문번호 검증
        if(category.isRequireOrderNumber() && request.orderId() == null) {
            throw new DomainException(ErrorType.QUESTION_ORDER_NUMBER_REQUIRED);
        }

        // 주문 정보 조회
        Order order = null;
        if(request.orderId() != null) {
            order = orderRepository.findByOrderNumber(request.orderId())
                    .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

            // 본인의 주문인지 조회
            if(!order.getUserId().equals(user.getId())) {
                throw new DomainException(ErrorType.MATCH_NOT_USER);
            }
        }

        Question question = QuestionRequest.toEntity(request, category, user, order);
        questionRepository.save(question);
        return question.getId();
    }

    /**
     * 문의사항 id로 해당 문의사항을 조회하는 메서드
     *
     * @param user          사용자 정보
     * @param questionId    문의사항 id
     * @return  사용자가 작성한 문의사항
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<QuestionResponse> getQuestion(User user, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DomainException(ErrorType.QUESTION_NOT_FOUND));

        if(!question.getUser().getId().equals(user.getId())) {
            throw new DomainException(ErrorType.MATCH_NOT_USER);
        }

        return ResponseEntity.ok(QuestionResponse.fromQuestion(question));
    }

    /**
     * 사용자 정보를 기반으로 해당 사용자가 작성한 문의사항 조회 메서드
     *
     * @param user  사용자 정보
     * @return 사용자가 작성한 문의사항 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<QuestionResponse>> getMyQuestion(User user) {
        List<Question> questions = questionRepository.findAllByUserIdOrderByCreatedAt(user.getId());

        return ResponseEntity.ok(
                questions.stream()
                        .map(QuestionResponse::fromQuestion)
                        .toList()
        );
    }
}
