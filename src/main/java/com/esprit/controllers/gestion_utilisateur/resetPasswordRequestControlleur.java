package com.esprit.controllers.gestion_utilisateur;



import com.esprit.models.utilisateur.Utilisateur;
import com.esprit.services.utilisateur.AdminService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class resetPasswordRequestControlleur {
   private  Utilisateur utilisateur;
    @FXML
    private TextField emailField;

    @FXML
    private Button sendCodeButton;

    @FXML
    private Label messageLabel;

    private AdminService adminService = new AdminService();

    @FXML
    public void initialize() {
        System.out.println("ResetPasswordRequestController initialized!");
    }

    @FXML
    private void handleSendCodeButtonAction() throws SQLException {
        String email = emailField.getText();
        utilisateur=adminService.getUserByEmail(email);
        if (email.isEmpty()) {
            messageLabel.setText("Please enter your email address.");
        } else if (adminService.getUserByEmail(email)==null) {
            messageLabel.setText("User with this email does not exist.");
        } else {
            // Send verification email

            utilisateur=adminService.getUserByEmail(email);
            // Redirect to the verification interface
            redirectToVerificationInterface(utilisateur);
        }
    }

    private void redirectToVerificationInterface(Utilisateur utilisateur) {
        try {
            // Load the verification interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/verifyemail.fxml"));
            Parent root = loader.load();

            // Pass the email address to the verification controller
            emailVerificationControlleur verifyEmailController = loader.getController();
            verifyEmailController.setUtilisateur(utilisateur);
            verifyEmailController.setResetPasswordContext(true);
            // Show the verification interface
            Stage stage = (Stage) sendCodeButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Verify Your Email");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
