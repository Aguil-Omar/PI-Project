package com.pi_dev.controllers.GestionEspace;

import com.pi_dev.models.GestionEsapce.*;
import com.pi_dev.services.TypeEspaceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierTypeEspace {
    @FXML
    private TextField mtype;
    @FXML
    private TextArea mdescription;
    private TypeEspace typeEspace;
    @FXML
    public void initialize() {
        TypeEspace initialTypeEspace = new TypeEspace("Initial Type", "Initial Description");
        setTypeEspace(initialTypeEspace);
    }
    public void setTypeEspace(TypeEspace typeEspace) {
        this.typeEspace = typeEspace;
        mtype.setText(typeEspace.getType());
        mdescription.setText(typeEspace.getDescription());
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
    public void ModifierType(ActionEvent actionEvent) {
        String type = mtype.getText();
        String description = mdescription.getText();


        if (type.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {

            typeEspace.setType(type);
            typeEspace.setDescription(description);


            TypeEspaceService pst = new TypeEspaceService();
            pst.modifier(typeEspace);


            showAlert("Succès", "Type d'espace modifié avec succès !");



        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification du type d'espace .");
            e.printStackTrace();
        }
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retour(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/AfficheTypeEspace.fxml");
    }
}
