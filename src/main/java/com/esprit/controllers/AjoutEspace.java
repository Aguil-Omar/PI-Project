package com.esprit.controllers;

import com.esprit.models.Disponibilite;
import com.esprit.models.Espace;
import com.esprit.services.EspaceService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class AjoutEspace {

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfTitre;

    @FXML
    private TextField tfLocalisation;

    @FXML
    private ComboBox<String> cbDisponible;

    @FXML
    public void initialize() {
        // Populate ComboBox with ENUM values
        cbDisponible.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
    }

    @FXML
    void AddEspace(ActionEvent event) throws IOException {
        String nom = tfNom.getText();
        String titre = tfTitre.getText();
        String localisation = tfLocalisation.getText();
        Disponibilite etat = Disponibilite.valueOf(cbDisponible.getValue()); // Get selected value

        if (nom.isEmpty() || titre.isEmpty() || localisation.isEmpty() || etat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.show();
            return;
        }

        EspaceService es = new EspaceService();
        es.ajouter(new Espace(nom, titre, localisation, Disponibilite.valueOf(cbDisponible.getValue()), 1));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Espace ajouté avec succès !");
        alert.show();

        // Redirect to the display page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheEspace.fxml"));
        Parent root = loader.load();
        tfNom.getScene().setRoot(root);
    }
}
