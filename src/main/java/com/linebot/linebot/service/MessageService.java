package com.linebot.linebot.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public String createReplyMessage(String receivedMessage) {
        return "How may I help you? " + receivedMessage;
    }
}