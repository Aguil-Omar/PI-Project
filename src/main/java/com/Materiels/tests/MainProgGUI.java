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
            // Charger l'interface principale (MainInterface.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();

            // Définir le titre de la fenêtre
            primaryStage.setTitle("Gestion des Matériels et Types de Matériels");

            // Créer une scène avec la racine chargée
            Scene scene = new Scene(root, 800, 600); // Ajustez la taille selon vos besoins
            primaryStage.setScene(scene);

            // Afficher la fenêtre
            primaryStage.show();

        } catch (Exception e) {
            // Gérer les erreurs de chargement
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface graphique.");
        }
    }

    public static void main(String[] args) {
        // Lancer l'application
        launch(args);
    }
}