package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.services.ProgService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ModifProgramme {

    @FXML
    private TextField tfActivite;

    @FXML
    private TextField tfHeureDebut;

    @FXML
    private TextField tfHeureFin;

    private programme programme;
    private ProgService progService = new ProgService();

    /**
     * Méthode à appeler après le chargement du contrôleur pour initialiser les données.
     */
    public void initData(programme prog) {
        this.programme = prog;

        if (prog != null) {
            tfActivite.setText(prog.getActivite());
            tfHeureDebut.setText(prog.getHeurDebut().toString());
            tfHeureFin.setText(prog.getHeurFin().toString());
        }
    }

    @FXML
    public void modifProgramme(ActionEvent actionEvent) {
        if (tfActivite.getText().isEmpty() || tfHeureDebut.getText().isEmpty() || tfHeureFin.getText().isEmpty()) {
            showAlert("Attention", "Tous les champs sont obligatoires !", AlertType.WARNING);
            return;
        }

        try {
            LocalTime heureDebut = LocalTime.parse(tfHeureDebut.getText());
            LocalTime heureFin = LocalTime.parse(tfHeureFin.getText());

            // Mise à jour de l'objet programme existant
            programme.setActivite(tfActivite.getText());
            programme.setHeurDebut(heureDebut);
            programme.setHeurFin(heureFin);

            progService.modifier(programme);
            showAlert("Succès", "Programme modifié avec succès !", AlertType.INFORMATION);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherProgramme.fxml"));
                Parent root = loader.load();
                AfficherProgramme controller = loader.getController();
                //controller.initialize(programme);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Programme");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Veuillez entrer l'heure au format HH:mm:ss (ex: 14:30:00).", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
