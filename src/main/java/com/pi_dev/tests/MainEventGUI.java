package com.pi_dev.tests;

import com.pi_dev.controllers.EvenementController.ModifEvenement;
import com.pi_dev.controllers.EvenementController.SuppEvenement;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.pi_dev.controllers.CommentController.CommentController;


import java.io.IOException;
import java.util.List;

public class MainEventGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Charger l'interface d'ajout d'événement (AjoutEvenement.fxml)
       // FXMLLoader loaderAjout = new FXMLLoader(getClass().getResource("/InterfaceComment/comment.fxml"));
        FXMLLoader loaderAjout = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
        Parent rootAjout = loaderAjout.load();
        Scene sceneAjout = new Scene(rootAjout);
        primaryStage.setScene(sceneAjout);
        primaryStage.setTitle("Ajouter un événement");
        primaryStage.show();

        // Nous allons ajouter un bouton ou une action pour tester l'affichage après l'ajout
      //  afficherEvenements();

    }

    // Méthode pour tester l'affichage des événements
    private void afficherEvenements() {
        try {
            // Charger la vue d'affichage des événements (AfficherEvenement.fxml)
            FXMLLoader loaderAfficher = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
            Parent rootAfficher = loaderAfficher.load();
            Scene sceneAfficher = new Scene(rootAfficher);
            Stage stageAfficher = new Stage();
            stageAfficher.setScene(sceneAfficher);
            stageAfficher.setTitle("Afficher les événements");

            // Récupérer le conteneur de la scène (ici un AnchorPane)
            AnchorPane anchorPane = (AnchorPane) rootAfficher; // Assurez-vous que le root est un AnchorPane

            // Ajouter un HBox pour organiser les boutons
            HBox buttonBox = new HBox(10); // Espacement de 10 entre les boutons
            buttonBox.setLayoutX(10);
            buttonBox.setLayoutY(10);

            // Ajouter un bouton de test pour simuler la suppression d'un événement
            Button deleteTestButton = new Button("Supprimer un événement test");
            deleteTestButton.setOnAction(event -> {
                // Simuler un événement à supprimer
                EvenService evenService = new EvenService();
                List<evenement> evenements = evenService.rechercher(); // Récupérer tous les événements

                if (!evenements.isEmpty()) {
                    evenement evenementASupprimer = evenements.get(0); // Récupérer le premier événement

                    // Utiliser le contrôleur SuppEvenement pour supprimer l'événement
                    SuppEvenement suppEvenement = new SuppEvenement();
                    suppEvenement.supprimerEvenement(evenementASupprimer); // Supprimer l'événement
                    loadEvenements(); // Rafraîchir la liste des événements après suppression
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setContentText("Aucun événement à supprimer.");
                    alert.showAndWait();
                }
            });

            // Ajouter un bouton de test pour simuler la modification d'un événement
            Button modifyTestButton = new Button("Modifier un événement test");
            modifyTestButton.setOnAction(event -> {
                // Simuler un événement à modifier
                EvenService evenService = new EvenService();
                List<evenement> evenements = evenService.rechercher(); // Récupérer tous les événements

                if (!evenements.isEmpty()) {
                    evenement evenementAModifier = evenements.get(0); // Récupérer le premier événement

                    // Ouvrir la page de modification avec l'événement sélectionné
                    ouvrirPageModification(evenementAModifier);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setContentText("Aucun événement à modifier.");
                    alert.showAndWait();
                }
            });

            // Ajouter les boutons dans le HBox
            buttonBox.getChildren().addAll(deleteTestButton, modifyTestButton);

            // Ajouter le HBox dans l'AnchorPane
            anchorPane.getChildren().add(buttonBox);

            stageAfficher.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la page de modification
    private void ouvrirPageModification(evenement evenement) {
        try {
            // Charger la page de modification (ModifEvenement.fxml)
            FXMLLoader loaderModif = new FXMLLoader(getClass().getResource("/InterfaceEvenement/ModifEvenement.fxml"));
            Parent rootModif = loaderModif.load();
            Scene sceneModif = new Scene(rootModif);
            Stage stageModif = new Stage();
            stageModif.setScene(sceneModif);
            stageModif.setTitle("Modifier un événement");

            // Passer l'événement sélectionné au contrôleur de modification
            ModifEvenement controller = loaderModif.getController();
            controller.setEvenement(evenement);

            stageModif.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur s'est produite lors du chargement de la page de modification.");
            alert.showAndWait();
        }
    }

    // Méthode pour rafraîchir l'affichage des événements (par exemple, après suppression)
    private void loadEvenements() {
        try {
            FXMLLoader loaderAfficher = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
            Parent rootAfficher = loaderAfficher.load();
            Scene sceneAfficher = new Scene(rootAfficher);
            Stage stageAfficher = new Stage();
            stageAfficher.setScene(sceneAfficher);
            stageAfficher.setTitle("Afficher les événements");
            stageAfficher.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}