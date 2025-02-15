package com.esprit.controllers;


import com.esprit.models.Adresse;
import com.esprit.models.Role;
import com.esprit.models.Utilisateur;
import com.esprit.services.AdminService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class testcontroller {

    @FXML
    private TextField nomField, prenomField, motDePasseField, emailField, telField, codePostalField, regionField;
    @FXML
    private RadioButton adminRadio, orgRadio, partRadio;
    @FXML
    private ToggleGroup roleGroup;
    @FXML
    private Button ajouterButton;

    private AdminService adminService = new AdminService();

    @FXML
    public void initialize() {
        roleGroup = new ToggleGroup();
        adminRadio = new RadioButton("Admin");
        orgRadio = new RadioButton("Organisateur");
        partRadio = new RadioButton("Participant");
        adminRadio.setToggleGroup(roleGroup);
        orgRadio.setToggleGroup(roleGroup);
        partRadio.setToggleGroup(roleGroup);
    }



    private Role getSelectedRole() {
        if (adminRadio.isSelected()) {
            return Role.ADMIN;
        } else if (orgRadio.isSelected()) {
            return Role.ORGANISATEUR;
        } else {
            return Role.PARTICIPANT;
        }
    }
    @FXML
    private void ajouterUtilisateur() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String motDePasse = motDePasseField.getText();
        String email = emailField.getText();
        String tel = telField.getText();
        String codePostalStr = codePostalField.getText();
        String region = regionField.getText();

        Role role = getSelectedRole();

        // Input validation
        if (nom.isEmpty() || prenom.isEmpty() || motDePasse.isEmpty() || email.isEmpty() || tel.isEmpty() || codePostalStr.isEmpty() || region.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        try {
            int codePostal = Integer.parseInt(codePostalStr);
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

        Adresse adresse = new Adresse(Integer.parseInt(codePostalStr), region);
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, motDePasse, role, adresse, tel);
        adminService.ajouter(utilisateur);

        clearFields();
        showAlert("Succès", "Utilisateur ajouté avec succès !");
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
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
