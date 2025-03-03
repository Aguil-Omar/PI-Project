package com.pi_dev.controllers.gestion_utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

        // Validate input fields
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Authenticate user
        Utilisateur user = adminService.authentifier(email, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Email ou mot de passe incorrect.");
            return;
        }

        // Successful login
        showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue, " + user.getNom() + "!");


        // Redirect based on user role
        String fxmlPath = null;
        switch (user.getRole()) {
            case ADMIN:
                fxmlPath = "/InterfaceUtilisateur/readUtilisateur.fxml";
                break;
            case PARTICIPANT:
                fxmlPath = "/InterfaceEvenement/PageAcceuil.fxml";
                break;

            case ORGANISATEUR:
                fxmlPath = "/InterfaceEvenement/PageAcceuil.fxml";
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Rôle inconnu", "Le rôle de l'utilisateur n'est pas reconnu.");
                return;
        }

        // Load the FXML file and set the new scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pass the user object to the next controller (if needed)
            Object controller = loader.getController();


            // Get the current stage
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger l'interface utilisateur.");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/resetPasswordRequest.fxml"));
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

