package com.linebot.linebot.service;

import com.linebot.linebot.lib.DifyPostClient;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MessageService {
    public String createReplyMessage(String receivedMessage) throws IOException {
        DifyPostClient difyPostClient = new DifyPostClient("testUser");

        return difyPostClient.callDifyAPI(receivedMessage);
    }
}