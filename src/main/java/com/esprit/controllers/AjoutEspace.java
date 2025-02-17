package com.esprit.controllers;

import com.esprit.models.Disponibilite;
import com.esprit.models.Espace;
import com.esprit.models.TypeEspace;
import com.esprit.services.EspaceService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class AjoutEspace {

    @FXML
    private TextField tfNom, tfLocalisation, tfType;

    @FXML
    private TextArea taDescription;

    @FXML
    private ComboBox<String> cbDisponible;

    @FXML
    private Button Bajouter;

    private EspaceService espaceService = new EspaceService();

    private AfficheEspace afficheEspaceController;  // Reference to AfficheEspaceController

    @FXML
    public void initialize() {
        // Populate ComboBox with ENUM values
        cbDisponible.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
    }

    @FXML
    private void AddEspace(ActionEvent event) throws IOException {
        // Retrieving input data
        String nom = tfNom.getText();
        String localisation = tfLocalisation.getText();

        // Validate ComboBox selection for state
        String selectedState = cbDisponible.getValue();
        if (selectedState == null) {
            showAlert("Erreur", "Veuillez sélectionner l'état de l'espace");
            return;
        }
        Disponibilite etat = Disponibilite.valueOf(selectedState);

        // Create TypeEspace object (only if it doesn't already exist in the DB)
        String typeName = tfType.getText();
        String typeDescription = taDescription.getText();
        if (typeName == null || typeDescription == null || typeName.isEmpty() || typeDescription.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir les champs Type et Description");
            return;
        }

        // Fetch or create the TypeEspace object from the database
        TypeEspace typeEspace = espaceService.getTypeEspaceByTypeAndDescription(typeName, typeDescription);
        if (typeEspace == null) {
            // If the TypeEspace does not exist, create and save it
            typeEspace = new TypeEspace(typeName, typeDescription);
            espaceService.saveTypeEspace(typeEspace);
        }

        // Input validation for Espace fields
        if (nom.isEmpty() || localisation.isEmpty() || etat == null || typeEspace == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        // Creating the Espace object
        Espace espace = new Espace(nom, localisation, etat, typeEspace);

        // Adding the Espace through the service
        espaceService.ajouter(espace);  // Ensure the method `ajouter` is properly defined in EspaceService

        // Clear fields after submission
        clearFields();

        // Show success message
        showAlert("Succès", "Espace ajouté avec succès !");

        // Redirect to the display page after adding the space
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheEspace.fxml"));
        Parent root = loader.load();
        tfNom.getScene().setRoot(root);
        AfficheEspace ap = loader.getController();
        ap.refreshTableView();  // Refresh the table view to show the new data
    }

    // Utility method to show alerts like in your friend's code
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear all fields after submission
    private void clearFields() {
        tfNom.clear();
        tfLocalisation.clear();
        tfType.clear();
        taDescription.clear();
        cbDisponible.setValue(null);  // Clear ComboBox
    }
}

    // Setter for AfficheEspaceController, so that we can pass it from the main app

