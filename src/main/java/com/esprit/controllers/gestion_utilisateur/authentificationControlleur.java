package com.esprit.controllers.gestion_utilisateur;

import com.esprit.services.utilisateur.AdminService;
import com.esprit.models.utilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class authentificationControlleur {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Label forgotPasswordLabel;

    @FXML
    private Label signUpLabel;

    @FXML
    private ImageView emailIcon;

    @FXML
    private ImageView passwordIcon;

    private final AdminService adminService = new AdminService();

    @FXML
    private HBox rootHBox;

    @FXML
    private VBox leftPanee;

    @FXML
    private VBox rightPane;


    @FXML
    public void initialize() {
        // Écouteur pour redimensionner dynamiquement les panneaux

    }


    @FXML
    private void handleSignInButtonAction() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur user = adminService.authentifier(email, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Email ou mot de passe incorrect.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue, " + user.getNom() + "!");
            // Redirect to the main application screen here
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Get the stage to apply styling
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alert.showAndWait();
    }

    public  void redirectToVerificationInterface() {
        try {
            // Load the verification interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resetPasswordRequest.fxml"));
            Parent root = loader.load();

            // Pass the email address to the verification controller


            // Show the verification interface
            Stage stage = (Stage) forgotPasswordLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Verify Your Email");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
