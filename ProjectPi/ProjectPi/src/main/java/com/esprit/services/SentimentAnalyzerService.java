package com.esprit.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SentimentAnalyzerService {
    public static String analyzeSentiment(String text) {
        try {
            // Get the absolute path to sentiment_analysis.py
            String pythonScriptPath = new File("C:\\Users\\omar\\OneDrive\\Bureau\\ProjectPi\\ProjectPi\\src\\main\\resources\\script\\sentimentAnalysis.py").getAbsolutePath();
            // Run Python script with the comment as an argument
            ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath, text);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String result = reader.readLine();
            // Determine sentiment result
            if (result != null && result.contains("positive")) {
                return "positive";
            } else if (result != null && result.contains("negative")) {
                return "negative";
            } else {
                return "neutral";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing sentiment";
        }
    }
}
