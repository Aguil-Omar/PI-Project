package com.pi_dev.controllers.CommentController;

import com.pi_dev.services.ChatBotService;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class chatController {
    @FXML
    private VBox chatArea;
    @FXML
    private TextField userInput;

    private final ChatBotService chatbotService = new ChatBotService();

    @FXML
    private void sendMessage() {
        String userMessage = userInput.getText();
        if (!userMessage.isEmpty()) {
            Text userText = new Text(userMessage );
            userText.setStyle(
                    "-fx-fill: white; " +
                            "-fx-font-size: 12px; " +
                            "-fx-font-weight: bold;"
            );
            TextFlow userTextFlow = new TextFlow(userText);
            userTextFlow.setStyle(
                    "-fx-background-color: #0078b8; " +
                            "-fx-background-radius: 15px; " +
                            "-fx-padding: 5px 10px;"
            );

            // Chatbot response
            String response = chatbotService.getResponse(userMessage);
            response.replaceAll("\n","");
            Text botText = new Text(response);
            botText.setStyle(
                    "-fx-fill: rgba(0,0,0,0.5); " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;"
            );
            TextFlow botTextFlow = new TextFlow(botText);
            botTextFlow.setStyle(
                    "-fx-background-color: rgb(147,225,240); " +
                            "-fx-background-radius: 15px; " +
                            "-fx-padding: 5px 10px;"
            );

            // Add messages to the chat area
            chatArea.getChildren().addAll(userTextFlow, botTextFlow);

            userInput.clear();
        }
    }
}