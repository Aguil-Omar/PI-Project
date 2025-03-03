package com.pi_dev.controllers.CommentController;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.services.SentimentAnalyzerService;
import com.pi_dev.services.commentaireService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.List;

public class StatisticsController {
    @FXML
    private PieChart sentimentPieChart;

    private commentaireService commentService = new commentaireService();

    @FXML
    public void initialize() {
        System.out.print("hello");
        updateStatistics();
    }

    private void updateStatistics() {
        // Fetch all comments from the database
        List<commentaire> comments = commentService.afficher();

        // Initialize counters for sentiment analysis
        int positiveCount = 0;
        int negativeCount = 0;
        int neutralCount = 0;

        // Analyze sentiment for each comment
        for (commentaire comment : comments) {
            String sentiment = SentimentAnalyzerService.analyzeSentiment(comment.getComment());
            switch (sentiment) {
                case "positive":
                    positiveCount++;
                    break;
                case "negative":
                    negativeCount++;
                    break;
                default:
                    neutralCount++;
                    break;
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Positive", positiveCount),
                new PieChart.Data("Negative", negativeCount),
                new PieChart.Data("Neutral", neutralCount)
        );

        sentimentPieChart.setData(pieChartData);
        sentimentPieChart.setTitle("Comment Sentiment Distribution");
    }}