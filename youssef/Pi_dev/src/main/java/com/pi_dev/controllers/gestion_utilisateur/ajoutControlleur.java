package com.pi_dev.controllers.gestion_utilisateur;

import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ajoutControlleur {

    @FXML
    private TextField nomField, prenomField, motDePasseField, emailField, telField, codePostalField, regionField;
    @FXML
    private RadioButton adminRadioButton, organisateurRadioButton, participantRadioButton;
    @FXML
    private ToggleGroup roleGroup;
    @FXML
    private Button ajouterButton, browseImageButton;
    @FXML
    private ImageView imageView;
@FXML
private Button cancelButton;
    private AdminService adminService = new AdminService();
    private String imagePath;

    @FXML
    public void initialize() {
        roleGroup = new ToggleGroup();
        adminRadioButton.setToggleGroup(roleGroup);
        organisateurRadioButton.setToggleGroup(roleGroup);
        participantRadioButton.setToggleGroup(roleGroup);
    }

    private Role getSelectedRole() {
        if (adminRadioButton.isSelected()) {
            return Role.ADMIN;
        } else if (organisateurRadioButton.isSelected()) {
            return Role.ORGANISATEUR;
        } else {
            return Role.PARTICIPANT;
        }
    }

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers d'image", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            System.out.println("Image path: " + imagePath);
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }
    private void redirectToReadUtilisateur() {
        try {
            // Load the readUtilisateur.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/readUtilisateur.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la redirection : " + e.getMessage());
        }
    }

    @FXML
    private void cancelAdd() {
        // Reload the current page with the original utilisateur details
          // Reset form fields with current utilisateur data

        this.redirectToReadUtilisateur();
    }

    @FXML
    private void ajouterUtilisateur() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String motDePasse = motDePasseField.getText();
        String email = emailField.getText();
        String tel = telField.getText();
        String codePostalStr = codePostalField.getText();
        String region = regionField.getText();
        Role role = getSelectedRole();

        // Input validation
        if (nom.isEmpty() || prenom.isEmpty() || motDePasse.isEmpty() || email.isEmpty() || tel.isEmpty() ||
                codePostalStr.isEmpty() || region.isEmpty() || imagePath == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs et sélectionner une image");
            return;
        }

        int codePostal;
        try {
            codePostal = Integer.parseInt(codePostalStr);
            if (codePostal < 0) {
                showAlert("Erreur", "Code postal doit être un entier positif");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Code postal doit être un entier");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Erreur", "Email doit contenir @");
            return;
        }

        // Create Adresse and Utilisateur objects
        Adresse adresse = new Adresse(codePostal, region);
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, motDePasse, role, adresse, tel, imagePath);

        // Add user using AdminService (file upload logic is handled in the service)
        try {
            adminService.ajouter(utilisateur);
            showAlert("Succès", "Utilisateur ajouté avec succès !");
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        motDePasseField.clear();
        emailField.clear();
        telField.clear();
        codePostalField.clear();
        regionField.clear();
        roleGroup.selectToggle(null);
        imageView.setImage(null);
        imagePath = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateAfficheUtilisateur(ActionEvent actionEvent) {
        this.redirectToReadUtilisateur();
    }
}