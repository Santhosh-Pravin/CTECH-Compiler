package com.ctechcompiler.backend.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VivaService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public String generateVivaQuestionFromCode(String studentCode) throws IOException {
        String prompt = "You are a computer science professor conducting a viva (oral exam). " +
                "Analyze the following Java code snippet and generate one single, concise, open-ended question " +
                "to test the student's understanding of their own code. Do not ask for definitions. " +
                "Ask about their specific implementation. Here is the code:\n\n" + studentCode;

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);
        JSONArray parts = new JSONArray().put(textPart);
        JSONObject contents = new JSONObject().put("parts", parts);
        JSONObject payload = new JSONObject().put("contents", new JSONArray().put(contents));

        RequestBody body = RequestBody.create(
                payload.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        // --- THIS IS THE CORRECTED URL ---
        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + geminiApiKey)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBodyString = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " - " + responseBodyString);
            }

            JSONObject responseBody = new JSONObject(responseBodyString);
            return responseBody.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim();
        }
    }

    public JSONObject evaluateVivaAnswer(String studentCode, String question, String answer) throws IOException {
        String prompt = "You are a computer science professor evaluating a student's viva answer. " +
                "Based on the original code, the question you asked, and the student's answer, " +
                "provide a score out of 10 and brief, constructive feedback. " +
                "Return your response as a JSON object with two keys: \"score\" (an integer) and \"feedback\" (a string).\n\n" +
                "Original Code:\n" + studentCode + "\n\n" +
                "Question Asked:\n" + question + "\n\n" +
                "Student's Answer:\n" + answer;

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);
        JSONArray parts = new JSONArray().put(textPart);
        JSONObject contents = new JSONObject().put("parts", parts);
        JSONObject payload = new JSONObject().put("contents", new JSONArray().put(contents));

        RequestBody body = RequestBody.create(payload.toString(), MediaType.get("application/json; charset=utf-8"));

        // --- THIS IS THE CORRECTED URL ---
        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + geminiApiKey)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseString = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " - " + responseString);
            }

            String jsonText = new JSONObject(responseString).getJSONArray("candidates")
                    .getJSONObject(0).getJSONObject("content")
                    .getJSONArray("parts").getJSONObject(0).getString("text");

            jsonText = jsonText.replace("```json", "").replace("```", "").trim();
            return new JSONObject(jsonText);
        }
    }
}