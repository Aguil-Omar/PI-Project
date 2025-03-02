package com.esprit.controllers.gestion_utilisateur;

import com.esprit.models.utilisateur.Utilisateur;
import com.esprit.services.utilisateur.AdminService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class profilControlleur {

    private  AdminService adminService = AdminService.getInstance();
    private Utilisateur utilisateur=adminService.getLoggedUser();
    private String imagePath;

    @FXML
    private ImageView profileImage;

    @FXML
    private TextField nomField, prenomField, emailField, phoneField, roleField, regionField, postalCodeField;

    @FXML
    private Button changeImageButton, cancelButton, updateButton;
    @FXML
    private ImageView userIcon;

    @FXML
    private Label loggedUserName;
    @FXML
    public void initialize() {
        roleField.setEditable(false);
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        nomField.setText(utilisateur.getNom());
        prenomField.setText(utilisateur.getPrenom());
        emailField.setText(utilisateur.getEmail());
        phoneField.setText(utilisateur.getTel());
        roleField.setText(utilisateur.getRole().toString());
        regionField.setText(utilisateur.getAdresse().getRegion());
        postalCodeField.setText(String.valueOf(utilisateur.getAdresse().getCodePostal()));

        if (utilisateur.getImageUrl() != null && !utilisateur.getImageUrl().isEmpty()) {
            imagePath = "file:/C:/wamp64/www/" + utilisateur.getImageUrl();
            profileImage.setImage(new Image(imagePath));
        } else {
            profileImage.setImage(new Image("file:/C:/wamp64/www/utilisateur_images/nullimage.png"));
        }
        // Set logged-in user's name
        loggedUserName.setText(utilisateur.getNom() + " " + utilisateur.getPrenom());
        System.out.println(loggedUserName);
        // Load Profile Picture
        if (utilisateur.getImageUrl() != null && !utilisateur.getImageUrl().isEmpty()) {
            String imagePath = "file:/C:/wamp64/www/" + utilisateur.getImageUrl();
            userIcon.setImage(new Image(imagePath));
        } else {
            userIcon.setImage(new Image("file:/C:/wamp64/www/utilisateur_images/default.png")); // Default profile icon
        }
    }

    @FXML
    private void handleChangeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
            utilisateur.setImageUrl(imagePath);
        }
    }

    @FXML
    private void handleCancel() {
        redirectToReadUtilisateur();
    }

    @FXML
    private void handleUpdate() {
        if (validateInputs()) {
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setEmail(emailField.getText());
            utilisateur.setTel(phoneField.getText());
            utilisateur.getAdresse().setRegion(regionField.getText());
            utilisateur.getAdresse().setCodePostal(Integer.parseInt(postalCodeField.getText()));

            if (imagePath != null) {
                utilisateur.setImageUrl(imagePath);
            }

            try {
                adminService.modifier(utilisateur);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur mis à jour avec succès !");
                redirectToReadUtilisateur();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Veuillez remplir tous les champs correctement.");
        }
    }

    private boolean validateInputs() {
        return !nomField.getText().trim().isEmpty() &&
                !prenomField.getText().trim().isEmpty() &&
                emailField.getText().contains("@") &&
                phoneField.getText().matches("\\d{8,15}") &&
                !regionField.getText().trim().isEmpty() &&
                postalCodeField.getText().matches("\\d{4,6}");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToReadUtilisateur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readUtilisateur.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la redirection : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
