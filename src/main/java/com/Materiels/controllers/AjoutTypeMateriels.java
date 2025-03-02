package com.Materiels.controllers;

import com.Materiels.models.TypeMateriels;
import com.Materiels.services.TypeMaterielsServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjoutTypeMateriels {

    @FXML
    private TextField txtNomM;

    @FXML
    private TextField txtDescription;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnAnnuler;

    @FXML
    public void initialize() {

    }

    @FXML
    void ajout(ActionEvent event) {

        String nomM = txtNomM.getText();
        String description = txtDescription.getText();


        if (nomM.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {

            TypeMateriels typeMateriel = new TypeMateriels(nomM, description);


            TypeMaterielsServices tms = new TypeMaterielsServices();
            tms.ajouter(typeMateriel);


            showAlert("Succès", "Type de matériel ajouté avec succès !");


            clearFields();
            naviguerVersMain();

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du type de matériel.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        naviguerVersMain();
    }

    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();
            Scene currentScene = btnAjout.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        txtNomM.clear();
        txtDescription.clear();
    }

}