package com.linebot.linebot.lib;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;

@PropertySource("classpath:application.properties")
public class DifyPostClient {
    private static final String WEB_API_ENDPOINT = "https://api.dify.ai/v1/chat-messages";

    @Value("${dify.api.key}")
    private String DIFY_API_KEY;

    @Getter
    private String conversation_id = "";

    final String userId;

    public DifyPostClient(String userId) {
        Environment env = null;
        this.DIFY_API_KEY = env.getProperty("dify.api.key");
        this.userId = userId;
    }

    public String callDifyAPI(String message) {
        System.out.println("doPost");
        System.out.println(DIFY_API_KEY);
        JsonNode res = doPost(message);

        String resMessage = res.get("answer").asText();
        if (conversation_id.isEmpty()) conversation_id = res.get("conversation_id").asText();

        return resMessage;
    }

    public JsonNode doPost(String message) {
        OkHttpClient client = new OkHttpClient();

        // JSONデータを作成
        String jsonData = String.format("""
                {
                    "inputs": {},
                    "query": "%s",
                    "response_mode": "blocking",
                    "conversation_id": "",
                    "user": "uchii"
                }
                """, message);

        // リクエストボディを作成
        RequestBody body = RequestBody.create(jsonData, MediaType.get("application/json; charset=utf-8"));

        // リクエストを作成
        Request request = new Request.Builder()
                .url(WEB_API_ENDPOINT)
                .header("Authorization", "Bearer " + DIFY_API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // リクエストを送信してレスポンスを取得
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // レスポンスボディを取得
            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody);

            // Jacksonを使用してJSONレスポンスを解析
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            System.out.println(jsonNode.toString());

            // JSONオブジェクトとしてフィールドにアクセス
            System.out.println("Query: " + jsonNode.get("conversation_id").asText());
            System.out.println("User: " + jsonNode.get("answer").asText());

            return jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

