package com.jishop.service;

public interface EmailSender {

    void send(String to, String subject, String body);
}
