package com.esprit.tests;

import com.esprit.controller.CommentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatBot.fxml"));
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/comment.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/statistics.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        //CommentController commentController = loader.getController();
        //scene.setUserData(commentController);

        primaryStage.setScene(scene);
        primaryStage.setTitle(" Chat bot");
        primaryStage.show();
    }
}
