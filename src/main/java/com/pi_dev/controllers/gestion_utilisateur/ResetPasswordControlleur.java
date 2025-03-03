package com.pi_dev.controllers.gestion_utilisateur;


import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ResetPasswordControlleur {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button resetButton;

    @FXML
    private Label messageLabel;

    private AdminService adminService = new AdminService();
    private Utilisateur utilisateur;

    // Set the email address (called from the previous controller)
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @FXML
    public void initialize() {
        System.out.println("ResetPasswordController initialized!");
    }

    @FXML
    private void handleResetButtonAction() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
        } else if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
        } else {
            // Update the user's password
            adminService.modifier(utilisateur);
            messageLabel.setText("Password reset successfully!");
            // Navigate to the sign-in screen or show a success message
        }
    }
}