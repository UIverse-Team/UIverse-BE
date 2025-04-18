package com.jishop.member.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.jishop.member.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSender {

    @Value("${aws.ses.mail}")
    private String mail;
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    // aws ses api 사용해서 이메일 전송하기
    @Override
    public void send(String to, String subject, String body) {
        Content bodyContent = new Content().withData(body).withCharset("UTF-8");    // 내용은 무엇인지
        Body emailBody = new Body().withHtml(bodyContent);                          // <- HTML로 전송, 본문 객체 생성

        Content subjectContent = new Content().withData(subject).withCharset("UTF-8");      // 제목은 무엇인지
        Message message = new Message().withSubject(subjectContent).withBody(emailBody);    // 이메일 전체 메시지를 구성하는 객체 생성

        Destination destination = new Destination().withToAddresses(to);            // 누구에게 보내는지
        SendEmailRequest request = new SendEmailRequest()                           // 이메일 전송 요청 객체 생성
                .withSource(mail)
                .withDestination(destination)
                .withMessage(message);

        amazonSimpleEmailService.sendEmail(request);
    }
}
