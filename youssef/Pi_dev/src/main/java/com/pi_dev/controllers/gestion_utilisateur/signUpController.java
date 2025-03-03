package com.pi_dev.controllers.gestion_utilisateur;

import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class signUpController {

    // Link UI components from the FXML file
    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField regionField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField telephoneField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label signInLabel;
    AdminService adminService=new AdminService();

    Utilisateur utilisateur=new Utilisateur();
    // Initialize method (optional)
    @FXML
    public void initialize() {
        System.out.println("SignUpController initialized!");


    }

    // Event handler for the Sign Up button
    @FXML
    private void handleSignUpButtonAction() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String region = regionField.getText();
        String postalCode = postalCodeField.getText();
        String telephone = telephoneField.getText();

        // Validate fields
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || region.isEmpty() || postalCode.isEmpty() || telephone.isEmpty()) {
            System.out.println("Please fill in all fields.");
        } else if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
        } else if (adminService.userExists(email)) {
            System.out.println("User with this email already exists.");
        } else {
            int postalcode = Integer.parseInt(postalCode);
            String imageUrl=null;
            Adresse adresse = new Adresse(postalcode, region);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);  // Create a new Utilisateur
            utilisateur.setEmail(email);
            utilisateur.setMotDePasse(password);
            utilisateur.setRole(Role.PARTICIPANT);
            utilisateur.setAdresse(adresse);
            utilisateur.setTel(telephone);
            utilisateur.setImageUrl(imageUrl);
            // Redirect to the verification interface
            System.out.println(utilisateur.toString());
            this.redirectToVerificationInterface(utilisateur);
        }
    }

    // Event handler for the Sign In link
    @FXML
    private void handleSignInAction(MouseEvent event) {
        System.out.println("Sign In clicked!");
        // Add logic to navigate to the sign-in screen
    }
@FXML
    private void redirectToVerificationInterface(Utilisateur utilisateur) {
        try {
            // Load the verification interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/verifyemail.fxml"));
            Parent root = loader.load();

            // Pass the email address to the verification controller
            emailVerificationControlleur verifyEmailController = loader.getController();
            verifyEmailController.setUtilisateur(utilisateur);
            verifyEmailController.setResetPasswordContext(false);
            System.out.println(utilisateur.toString());
            // Show the verification interface
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Verify Your Email");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}