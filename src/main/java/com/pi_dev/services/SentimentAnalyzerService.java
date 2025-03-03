package com.pi_dev.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SentimentAnalyzerService {
    public static String analyzeSentiment(String text) {
        try {

            String pythonScriptPath = new File("C:\\integration skander\\Pi_dev\\src\\main\\resources\\script\\sentimentAnalysis.py").getAbsolutePath();
            // Run Python script with the comment as an argument
            ProcessBuilder pb = new ProcessBuilder("python3", pythonScriptPath, text);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String result = reader.readLine();

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