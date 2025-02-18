package com.Materiels.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainProgGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();


            primaryStage.setTitle("Gestion des Matériels et Types de Matériels");


            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);


            primaryStage.show();

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface graphique.");
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}