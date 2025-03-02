package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.services.MaterielsServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class SupprimerMateriel {

    @FXML
    private Label lblMessage;

    private Materiels materiel;


    public void setMateriel(Materiels materiel) {
        this.materiel = materiel;
        lblMessage.setText("Êtes-vous sûr de vouloir supprimer le matériel : " + materiel.getNom() + " ?");
    }


    @FXML
    private void confirmer(ActionEvent event) {
        MaterielsServices ms = new MaterielsServices();
        ms.supprimer(materiel);

        showAlert("Succès", "Matériel supprimé avec succès !");


        naviguerVersMain();
    }


    @FXML
    private void annuler(ActionEvent event) {
        naviguerVersMain();
    }


    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/MainInterface.fxml"));
            Parent root = loader.load();
            lblMessage.getScene().setRoot(root);
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

}