package com.linebot.linebot.controllers;

import com.linebot.linebot.lib.DifyPostClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/test")
    public String test() throws IOException {
        DifyPostClient difyPostClient = new DifyPostClient("user1");

        String msg = "オブジェクト指向について分かりやすく教えて下さい。";

        String answer = difyPostClient.callDifyAPI(msg);

        return answer;
    }
}