package com.pi_dev.controllers.gestion_utilisateur;

import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class updateUtilisateur {

    @FXML
    private TextField nomField, prenomField, motDePasseField, emailField, telField, codePostalField, regionField;
    @FXML
    private RadioButton adminRadioButton, organisateurRadioButton, participantRadioButton;
    @FXML
    private ToggleGroup roleGroup;
    @FXML
    private Button updateButton, browseImageButton, cancelButton;
    @FXML
    private ImageView userImageView;

    private AdminService adminService = new AdminService();
    private String imagePath;
    private Utilisateur utilisateur;

    @FXML
    public void initialize() {
        // Initialize the role group
        roleGroup = new ToggleGroup();
        adminRadioButton.setToggleGroup(roleGroup);
        organisateurRadioButton.setToggleGroup(roleGroup);
        participantRadioButton.setToggleGroup(roleGroup);

        // Check if the utilisateur has an existing image and set it
        if (utilisateur != null && utilisateur.getImageUrl() != null && !utilisateur.getImageUrl().isEmpty()) {
            imagePath = "file:/C:/wamp64/www/" + utilisateur.getImageUrl();
            Image image = new Image(imagePath);
            userImageView.setImage(image);
        }
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

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        nomField.setText(utilisateur.getNom());
        prenomField.setText(utilisateur.getPrenom());
        motDePasseField.setText(utilisateur.getMotDePasse());
        emailField.setText(utilisateur.getEmail());
        telField.setText(utilisateur.getTel());
        codePostalField.setText(String.valueOf(utilisateur.getAdresse().getCodePostal()));
        regionField.setText(utilisateur.getAdresse().getRegion());

        // Set role
        if (utilisateur.getRole().equals(Role.ADMIN)) {
            adminRadioButton.setSelected(true);
        } else if (utilisateur.getRole().equals(Role.ORGANISATEUR)) {
            organisateurRadioButton.setSelected(true);
        } else {
            participantRadioButton.setSelected(true);
        }

        // Set image
        if (this.utilisateur != null && utilisateur.getImageUrl() != null && !utilisateur.getImageUrl().isEmpty()) {
            String imagePath = "file:/C:/wamp64/www/" + utilisateur.getImageUrl();
            Image image = new Image(imagePath);
            userImageView.setImage(image);
        } else {
            userImageView.setImage(new Image("file:/C:/wamp64/www/utilisateur_images/nullimage.png")); // Default image
        }
    }

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers d'image", "*.png", "*.jpg", "*.jpeg")
        );

        // Show file chooser and get the selected file
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Get the file path of the selected image
            imagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            userImageView.setImage(image);  // Set image preview

            // Optionally, you could store the file path in the Utilisateur object, to be saved later when updating
            utilisateur.setImageUrl(imagePath);
        }
    }

    @FXML
    private void updateUtilisateur() {
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
                codePostalStr.isEmpty() || region.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
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

        // Update Utilisateur details
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setEmail(email);
        utilisateur.setTel(tel);
        utilisateur.setRole(role);
        utilisateur.getAdresse().setCodePostal(codePostal);
        utilisateur.getAdresse().setRegion(region);

        // If the image path is updated, update the Utilisateur object
        if (imagePath != null) {
            utilisateur.setImageUrl(imagePath);
        }

        // Call update method in AdminService
        try {
            adminService.modifier(utilisateur); // Update utilisateur in the backend
            showAlert("Succès", "Utilisateur mis à jour avec succès !");
            this.redirectToReadUtilisateur();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void redirectToReadUtilisateur() {
        try {
            // Load the readUtilisateur.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/readUtilisateur.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la redirection : " + e.getMessage());
        }
    }

    @FXML
    private void cancelUpdate() {
        // Reload the current page with the original utilisateur details
        setUtilisateur(utilisateur);  // Reset form fields with current utilisateur data
        showAlert("Annulé", "Les modifications ont été annulées.");
        this.redirectToReadUtilisateur();
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
        userImageView.setImage(new Image("file:/C:/wamp64/www/utilisateur_images/nullimage.png")); // Reset to default image
        imagePath = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
