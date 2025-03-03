package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.services.ProgService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AjouterProgramme {

    @FXML
    private TextField tfActivite;

    @FXML
    private TextField tfHeureDebut;

    @FXML
    private TextField tfHeureFin;

    private int evenementId;  // Stocker l'ID de l'événement

    private ProgService progService = new ProgService();

    @FXML
    private void addProgramme(ActionEvent event) {
        if (tfActivite.getText().isEmpty() || tfHeureDebut.getText().isEmpty() || tfHeureFin.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", AlertType.ERROR);
            return;
        }

        try {
            LocalTime heureDebut = LocalTime.parse(tfHeureDebut.getText());
            LocalTime heureFin = LocalTime.parse(tfHeureFin.getText());

            programme newProgramme = new programme(
                    tfActivite.getText(),
                    heureDebut,
                    heureFin,
                    evenementId  // Utilisation de l'ID stocké
            );

            progService.ajouter(newProgramme);
            showAlert("Succès", "Programme ajouté avec succès !", AlertType.INFORMATION);
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Veuillez entrer l'heure au format HH:mm:ss (ex: 14:30:00).", AlertType.ERROR);
        }
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateDetails(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenementDetails.fxml"));
            Parent root = loader.load();
            tfActivite.getScene().setRoot(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
