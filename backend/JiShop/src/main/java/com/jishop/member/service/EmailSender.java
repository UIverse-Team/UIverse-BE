package com.jishop.member.service;

public interface EmailSender {

    void send(String to, String subject, String body);
}
