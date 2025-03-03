package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;

public class EvenementItem {
    @FXML
    private Label eventTitle;
    @FXML
    private Label eventDate;
    @FXML
    private Label eventDescription;
    @FXML
    private ImageView eventImage;
    @FXML
    private Label eventPrice;

    private evenement evt;

    public void setEvenement(evenement evt) {
        this.evt = evt;
        eventTitle.setText(evt.getTitre());
        eventDate.setText(evt.getDate().toString());
        eventDescription.setText(evt.getDescription());

        if (evt.getPrix() != 0) {
            eventPrice.setText("Prix: " + evt.getPrix() + " €");
        } else {
            eventPrice.setText("Prix non défini");
        }

        if (evt.getImage() != null && !evt.getImage().isEmpty()) {
            File imageFile = new File(evt.getImage());
            if (imageFile.exists()) {
                eventImage.setImage(new Image(imageFile.toURI().toString()));
            } else {
                eventImage.setImage(new Image(evt.getImage()));
            }
        }
    }

    @FXML
    public void handleMoreDetails(ActionEvent actionEvent) {
        if (evt != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenementDetails.fxml"));
                Parent root = loader.load();

                AfficherEvenementDetails controller = loader.getController();
                controller.initialize(evt);
                eventTitle.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors du chargement des détails de l'événement.", AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Aucun événement sélectionné.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour récupérer le titre de l'événement
    public Label getEventTitle() {
        return eventTitle;
    }

    // Méthode pour récupérer l'événement
    public evenement getEvenement() {
        return evt;
    }
}