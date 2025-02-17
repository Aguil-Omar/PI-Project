package com.pi_dev.controllers.EvenementController;

import com.pi_dev.controllers.CommentController.CommentController;
import com.pi_dev.models.GestionEvenement.evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import java.io.IOException;

public class AfficherEvenementDetails {

    @FXML
    private Text eventDetailsText;

    @FXML
    private Button addProgrammeButton;

    private evenement currentEvenement;  // Stocker l'événement actuel

    public void initialize(evenement evenement) {
        this.currentEvenement = evenement;
        eventDetailsText.setText(evenement.getId()+"Titre: " + evenement.getTitre() + "\nDescription: " + evenement.getDescription()
                + "\nDate: " + evenement.getDate() + "\nStatut: " + evenement.getStatut());
    }

    @FXML
    public void addProgramme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AjouterProgramme.fxml"));
            Parent root = loader.load();
            // Passer l'événement à la nouvelle page
            AjouterProgramme controller = loader.getController();
            controller.setEvenementId(this.currentEvenement.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Une erreur s'est produite lors du chargement de la page d'ajout de programme.");
        }

    }

    @FXML
    public void afficherProgrammes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherProgramme.fxml"));
            Parent root = loader.load();

            // Passer l'événement à la nouvelle page
            AfficherProgramme controller = loader.getController();
            controller.initialize(currentEvenement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Programmes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page des programmes.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateComments(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceComment/comment.fxml"));
            Parent root = loader.load();
            // Passer l'événement à la nouvelle page
            CommentController controller = loader.getController();
            controller.initialize(currentEvenement);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Programmes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page des programmes.");
        }
    }
}
