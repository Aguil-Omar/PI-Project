package com.Materiels.controllers;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.services.MaterielsServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;


public class ModifierMateriels{

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrix;
    @FXML
    private ComboBox<String> cmbEtat;

    private Materiels materiel;

    @FXML
    public void initialize() {

        cmbEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
    }


    public void setMateriel(Materiels materiel) {
        this.materiel = materiel;
        txtNom.setText(materiel.getNom());
        txtPrix.setText(String.valueOf(materiel.getPrix()));
        cmbEtat.setValue(materiel.getEtat().toString());
    }



    @FXML
    private void annuler() {
        naviguerVersMain();
    }


    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();
            txtNom.getScene().setRoot(root);
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

    public void update(ActionEvent event) {
        String nom = txtNom.getText();
        String prixStr = txtPrix.getText();
        String etatStr = cmbEtat.getValue();

        if (nom.isEmpty() || prixStr.isEmpty() || etatStr == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            Disponibilte etat = Disponibilte.valueOf(etatStr);


            materiel.setNom(nom);
            materiel.setPrix(prix);
            materiel.setEtat(etat);

            System.out.println(materiel);
            MaterielsServices ms = new MaterielsServices();
            ms.modifier(materiel);

            naviguerVersMain();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "L'état du matériel est invalide.");
        }
    }

}