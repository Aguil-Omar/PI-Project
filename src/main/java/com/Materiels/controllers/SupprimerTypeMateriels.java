package com.Materiels.controllers;

import com.Materiels.models.TypeMateriels;
import com.Materiels.services.TypeMaterielsServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class SupprimerTypeMateriels {

    @FXML
    private Label lblMessage;

    private TypeMateriels typeMateriel;

    // Méthode pour définir le type de matériel à supprimer
    public void setTypeMateriel(TypeMateriels typeMateriel) {
        this.typeMateriel = typeMateriel;
        lblMessage.setText("Êtes-vous sûr de vouloir supprimer le type de matériel : " + typeMateriel.getNomM() + " ?");
    }

    // Méthode pour confirmer la suppression
    @FXML
    private void confirmer(ActionEvent event) {
        // Supprimer le type de matériel via le service
        TypeMaterielsServices tms = new TypeMaterielsServices();
        tms.supprimer(typeMateriel);

        // Afficher un message de succès
        showAlert("Succès", "Type de matériel supprimé avec succès !");

        // Naviguer vers l'interface d'affichage
        naviguerVersMain();
    }

    // Méthode pour annuler la suppression
    @FXML
    private void annuler(ActionEvent event) {
        naviguerVersMain();
    }

    // Méthode pour naviguer vers l'interface d'affichage
    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();
            lblMessage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface d'affichage.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

}