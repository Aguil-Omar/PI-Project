package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.EvenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class AjoutEvenement  {

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
    @FXML
    private Label lblImagePath;
    @FXML
    private TextField tfPrix; // Ajout du champ Prix

    private String imagePath = null; // Stocke le chemin relatif de l’image enregistrée

    private EvenService evenService = new EvenService();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {


        cbStatut.getItems().addAll("Planifié", "En cours", "Terminé");
        cbCategorie.getItems().addAll(Categorie.values());
    }

    public boolean validateInputs() {
        if (tfTitre.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Le titre est obligatoire.");
            return false;
        }
        if (taDescription.getText().isEmpty()) {
            showAlert("Erreur de saisie", "La description est obligatoire.");
            return false;
        }
        if (dpDate.getValue() == null) {
            showAlert("Erreur de saisie", "La date est obligatoire.");
            return false;
        }
        if (cbStatut.getValue() == null) {
            showAlert("Erreur de saisie", "Le statut est obligatoire.");
            return false;
        }
        if (cbCategorie.getValue() == null) {
            showAlert("Erreur de saisie", "La catégorie est obligatoire.");
            return false;
        }
        if (tfPrix.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Le prix est obligatoire.");
            return false;
        }
        try {
            double prix = Double.parseDouble(tfPrix.getText());
            if (prix < 0) {
                showAlert("Erreur de saisie", "Le prix doit être un nombre positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le prix doit être un nombre valide.");
            return false;
        }
        if (imagePath == null) {
            showAlert("Erreur de saisie", "Veuillez importer une image pour l'événement.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addEvenement(ActionEvent event) {
        if (validateInputs()) {
            String titre = tfTitre.getText();
            String description = taDescription.getText();
            Date date = java.sql.Date.valueOf(dpDate.getValue());
            String statut = cbStatut.getValue();
            Categorie categorie = cbCategorie.getValue();
            double prix = Double.parseDouble(tfPrix.getText());

            // Création de l'événement avec le prix et l'image
            evenement newEvenement = new evenement(titre, description, date, statut, categorie, imagePath, prix);
            evenService.ajouter(newEvenement);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("L'événement a été ajouté avec succès !");
            alert.showAndWait();

            navigateAfficheEvent(event);
        }
    }

    public void navigateAfficheEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfTitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Événements");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String folderPath = "images";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName = file.getName();
                File destinationFile = new File(folder, fileName);
                Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                imagePath = folderPath + "/" + fileName;
                lblImagePath.setText(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                lblImagePath.setText("Erreur lors de l'importation !");
            }
        }
    }

}
