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

    AdminService adminService=new AdminService();

    Utilisateur utilisateur=new Utilisateur();
    @FXML
    public void initialize() {
        System.out.println("SignUpController initialized!");


    }

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
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setMotDePasse(password);
            utilisateur.setRole(Role.PARTICIPANT);
            utilisateur.setAdresse(adresse);
            utilisateur.setTel(telephone);
            utilisateur.setImageUrl(imageUrl);
            System.out.println(utilisateur.toString());
            this.redirectToVerificationInterface(utilisateur);
        }
    }

    @FXML
    private void handleSignInAction(MouseEvent event) {
        System.out.println("Sign In clicked!");
    }
@FXML
    private void redirectToVerificationInterface(Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/verifyemail.fxml"));
            Parent root = loader.load();
            emailVerificationControlleur verifyEmailController = loader.getController();
            verifyEmailController.setUtilisateur(utilisateur);
            verifyEmailController.setResetPasswordContext(false);
            System.out.println(utilisateur.toString());
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("Verify Your Email");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectTosignin(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setTitle("sign in");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}