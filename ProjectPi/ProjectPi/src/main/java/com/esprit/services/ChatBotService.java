package com.esprit.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatBotService {
    private static final Logger logger = Logger.getLogger(ChatBotService.class.getName());
    private static final String API_KEY = "AIzaSyAkRZjNUAfLVHPBKmTHy5BycCOdf02Ishs";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public String getResponse(String userMessage) {
        try {

            String requestBody = "{"
                    + "\"contents\": [{\"parts\": [{\"text\": \""
                    + "You are a helpful assistant who specializes in answering questions about events. "
                    + "You can also respond to general greetings like 'Hello', 'Hi', and 'How are you?'. "
                    + "If a user asks about something unrelated to events or greetings, respond with: "
                    + "'I can only provide information about events or have a friendly chat!' "
                    + "User: " + userMessage + "\"}]}],"
                    + "\"generationConfig\": {\"maxOutputTokens\": 100}"
                    + "}";


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return extractResponse(response.body());
            } else {
                logger.log(Level.SEVERE, "API request failed with status code: " + response.statusCode());
                return "Error: Unable to get a response from the chatbot.";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred: ", e);
            return "Error: " + e.getMessage();
        }
    }

    private String extractResponse(String jsonResponse) {
        try {
            int start = jsonResponse.indexOf("\"text\": \"") + 9;
            int end = jsonResponse.indexOf("\"", start);
            if (start > 8 && end > start) {
                return jsonResponse.substring(start, end);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to parse API response: ", e);
        }
        return "No response received.";
    }
}
