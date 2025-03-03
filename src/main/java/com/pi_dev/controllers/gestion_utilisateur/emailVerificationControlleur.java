package com.pi_dev.controllers.gestion_utilisateur;

import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.EmailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class emailVerificationControlleur {

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label messageLabel;

    private AdminService adminService = new AdminService();
    private EmailService emailService = new EmailService();

    private boolean isResetPassword = false;

    // Set the email address (called from the previous controller)
    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        if (utilisateur != null && utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty()) {
            System.out.println("Utilisateur défini : " + utilisateur.getEmail());
            emailService.sendVerificationEmail(utilisateur.getEmail());
        } else {
            System.out.println("Erreur : utilisateur ou email est null !");
        }

    }

    // Set the context (sign-up or reset password)
    public void setResetPasswordContext(boolean isResetPassword) {
        this.isResetPassword = isResetPassword;
    }

    @FXML
    public void initialize() {
        System.out.println("VerifyEmailController initialized!");


    }

    @FXML
    private void handleVerifyButtonAction() {
        String verificationCode = verificationCodeField.getText();

        if (verificationCode.isEmpty()) {
            messageLabel.setText("Please enter the verification code.");
        } else if (emailService.getGenertaedCode().equals(verificationCode)) {
            messageLabel.setText("Email verified successfully!");


            if (isResetPassword) {
                // Redirect to the "Change Password" interface
                loadChangePasswordInterface();
            } else {
                // Create the account (sign-up context)
                adminService.ajouter(utilisateur);
                this.loaddashboard();

                // Navigate to the next screen (e.g., dashboard or login)
                //loadDashboardInterface();
            }
        } else {
            messageLabel.setText("Invalid verification code. Please try again.");
        }

    }
    private void loaddashboard() {
        System.out.println("Redirecting to change password interface...");
        // Example: Load the change password FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/signin.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            Stage stage = (Stage) verificationCodeField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadChangePasswordInterface() {
        System.out.println("Redirecting to change password interface...");
        // Example: Load the change password FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/resetPassword.fxml"));
            Parent root = loader.load();
            ResetPasswordControlleur resetPasswordControlleur = loader.getController();
            resetPasswordControlleur.setUtilisateur(utilisateur);
            Scene scene = new Scene(root);
            Stage stage = (Stage) verificationCodeField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





