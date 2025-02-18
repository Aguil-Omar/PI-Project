package com.esprit.controllers;

import com.esprit.models.TypeEspace;
import com.esprit.services.TypeEspaceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AjoutTypeEspace {

    @FXML
    private TextField tfType; // TextField for "Type"

    @FXML
    private TextArea taDescription; // TextArea for "Description"

    @FXML
    private Button bSuivant; // Button for "Suivant"

    @FXML
    private Button bAnnuler; // Button for "Annuler"

    private TypeEspaceService typeEspaceService = new TypeEspaceService();

    // Method to add TypeEspace and switch to AfficheTypeEspace
    @FXML
    public void AddType() throws IOException {
        String type = tfType.getText();
        String description = taDescription.getText();

        if (type.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        TypeEspace newTypeEspace = new TypeEspace(type, description);
        typeEspaceService.ajouter(newTypeEspace);

        showAlert("Succès", "TypeEspace ajouté avec succès.", Alert.AlertType.INFORMATION);

        // Clear the form fields
        tfType.clear();
        taDescription.clear();

        // Switch to AfficheTypeEspace.fxml
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheTypeEspace.fxml"));
        Parent root = loader.load();

        // Set the new scene
        tfType.getScene().setRoot(root);


    }


    // Method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // "Annuler" button clears form fields
    @FXML
    public void cancel() {
        tfType.clear();
        taDescription.clear();
    }
}
