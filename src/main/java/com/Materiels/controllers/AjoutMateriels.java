package com.Materiels.controllers;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.services.MaterielsServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AjoutMateriels {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrix;

    @FXML
    private ComboBox<String> cmbEtat;

    @FXML
    private Button btnAjout;
    @FXML
    private Button btnAnnule ;

    @FXML
    public void initialize() {

        cmbEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
    }

    @FXML
    void ajout(ActionEvent event) {

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


            Materiels materiel = new Materiels(nom, prix, etat);


            MaterielsServices ms = new MaterielsServices();
            ms.ajouter(materiel);


            showAlert("Succès", "Matériel ajouté avec succès !");


            clearFields();
            try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root=loader.load();
                txtNom.getScene().setRoot(root);

            }

            catch (Exception e){e.printStackTrace();}
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "L'état du matériel est invalide.");
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
        txtNom.clear();
        txtPrix.clear();
        cmbEtat.getSelectionModel().clearSelection();
    }

}
