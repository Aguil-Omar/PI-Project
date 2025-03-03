package com.pi_dev.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class obsceneLanguageService {
    private static final String API_KEY = "AIzaSyAkRZjNUAfLVHPBKmTHy5BycCOdf02Ishs"; // Replace with your Gemini API key
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public static boolean containsObsceneLanguage(String text) {
        try {

            String prompt = "Analyze the following text and determine if it contains obscene language. "
                    + "The following words are considered obscene: zebi, miboun, mnayka, nayek, asba, aasba, 3asba,"
                    + " mnayek, ta7an, tahan, zokomk, kahba, 9ahba, qahba , kizebi, sorm, zok "
                    + "Respond with '1' if the text contains any of these words, or '0' if it does not. "
                    + "Text: " + text;

            String requestBody = "{"
                    + "\"contents\": [{\"parts\": [{\"text\": \"" + prompt + "\"}]}],"
                    + "\"generationConfig\": {\"maxOutputTokens\": 10}"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode candidatesNode = rootNode.path("candidates");
                if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                    JsonNode firstCandidate = candidatesNode.get(0);
                    JsonNode contentNode = firstCandidate.path("content");
                    JsonNode partsNode = contentNode.path("parts");
                    if (partsNode.isArray() && partsNode.size() > 0) {
                        String generatedText = partsNode.get(0).path("text").asText().trim();
                        return generatedText.equals("1");
                    }
                }
                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error checking for obscene language: " + e.getMessage());
            return false;
        }
    }
}
