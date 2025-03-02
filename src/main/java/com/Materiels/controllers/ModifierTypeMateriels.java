package com.Materiels.controllers;

import com.Materiels.models.TypeMateriels;
import com.Materiels.services.TypeMaterielsServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ModifierTypeMateriels {

    @FXML
    private TextField txtNomM;

    @FXML
    private TextField txtDescription;

    private TypeMateriels typeMateriel;

    @FXML
    public void initialize() {

    }


    public void setTypeMateriel(TypeMateriels typeMateriel) {
        this.typeMateriel = typeMateriel;
        txtNomM.setText(typeMateriel.getNomM());
        txtDescription.setText(typeMateriel.getDescription());
    }

    @FXML
    private void annuler(ActionEvent event) {
        naviguerVersMain();
    }


    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();
            txtNomM.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface d'affichage.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    @FXML
    public void update(ActionEvent event) {
        String nomM = txtNomM.getText();
        String description = txtDescription.getText();


        if (nomM.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {

            typeMateriel.setNomM(nomM);
            typeMateriel.setDescription(description);


            TypeMaterielsServices tms = new TypeMaterielsServices();
            tms.modifier(typeMateriel);


            showAlert("Succès", "Type de matériel modifié avec succès !");


            naviguerVersMain();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification du type de matériel.");
            e.printStackTrace();
        }
    }

}