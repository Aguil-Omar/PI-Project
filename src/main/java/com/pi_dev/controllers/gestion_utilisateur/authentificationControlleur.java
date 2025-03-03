package com.pi_dev.controllers.gestion_utilisateur;
import com.pi_dev.controllers.EvenementController.AcceuilEvenement;
import com.pi_dev.services.AdminService;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Label forgotPasswordLabel;

    private final AdminService adminService = new AdminService();

    @FXML
    public void initialize() {

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
            return;
        }


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

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene currentScene = emailField.getScene();
            currentScene.setRoot(root);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            Object controller = loader.getController();
            if (controller instanceof AcceuilEvenement) {
                ((AcceuilEvenement) controller).setLoggedUser(user);
            }
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
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alert.showAndWait();
    }

    public  void redirectToVerificationInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/resetPasswordRequest.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) forgotPasswordLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("Verify Your Email");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectTosignUp(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/signUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) forgotPasswordLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("sign up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

