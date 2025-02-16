package com.esprit.controllers;

import com.esprit.models.TypeEspace;
import com.esprit.services.TypeEspaceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AjoutTypeEspace {

    @FXML
    private TextField tfType;

    @FXML
    private TextArea taDescription;

    private final TypeEspaceService typeEspaceService = new TypeEspaceService();

    @FXML
    void AddType(ActionEvent event) throws IOException {
        String type = tfType.getText();
        String description = taDescription.getText();

        if (type.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.show();
            return;
        }

        TypeEspace typeEspace = new TypeEspace(type, description);
        typeEspaceService.ajouter(typeEspace);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Type d'espace ajouté avec succès !");
        alert.show();

        // Load AjoutEspace.fxml and pass TypeEspace data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEspace.fxml"));
        Parent root = loader.load();
        AjoutEspace ajoutEspaceController = loader.getController();
        ajoutEspaceController.setTypeEspace(typeEspace); // Pass the TypeEspace

        tfType.getScene().setRoot(root);

        // Clear fields
        tfType.clear();
        taDescription.clear();
    }

    @FXML
    void Cancel(ActionEvent event) {
        tfType.clear();
        taDescription.clear();
    }
}
