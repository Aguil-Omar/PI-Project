package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class AjoutEvenement {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextArea taDescription;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> cbStatut;
    @FXML
    private ComboBox<Categorie> cbCategorie;

    private EvenService evenService = new EvenService();

    @FXML
    public void initialize() {


        cbStatut.getItems().addAll("Planifié", "En cours", "Terminé");
        cbCategorie.getItems().addAll(Categorie.values());
    }


    @FXML
    private void addEvenement(ActionEvent event) {
        String titre = tfTitre.getText();
        String description = taDescription.getText();
        Date date = java.sql.Date.valueOf(dpDate.getValue());
        String statut = cbStatut.getValue();
        Categorie categorie = cbCategorie.getValue();

        // Création de l'événement
        evenement newEvenement = new evenement(titre, description, date, statut, categorie);

        // Ajouter à la base de données via le service
        evenService.ajouter(newEvenement);

        // Afficher une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("L'événement a été ajouté avec succès !");
        alert.showAndWait();

        // Changer la scène pour afficher les événements
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfTitre.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root)); // Changer la scène
            stage.setTitle("Liste des Événements");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
