package com.esprit.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class ProfanityChecker {
    private static final String API_URL = "https://api.api-ninjas.com/v1/profanityfilter?text=";
    private static final String API_KEY = "gj7f7nGtrMHl4tDggmNZbw==fMTpiAPFKcqX3GH4";

    public static boolean containsProfanity(String text) {
        System.out.println("hey");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String requestUrl = API_URL + java.net.URLEncoder.encode(text, "UTF-8");
            HttpGet request = new HttpGet(requestUrl);
            request.setHeader("X-Api-Key", API_KEY);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(jsonResponse);
                return rootNode.path("is_profane").asBoolean();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
