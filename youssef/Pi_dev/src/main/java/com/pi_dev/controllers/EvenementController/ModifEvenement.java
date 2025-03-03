package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.EvenService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date; // Pour java.util.Date

public class ModifEvenement {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextArea taDescription;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> cbStatut;
    @FXML
    private ComboBox<Categorie> cbCategorie;
    @FXML
    private TextField tfPrix; // Ajout du TextField pour le prix

    private evenement evenement;
    private EvenService evenService = new EvenService();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {

        // Initialisation des ComboBox
        cbStatut.getItems().addAll("Planifié", "En cours", "Terminé");
        cbCategorie.getItems().addAll(Categorie.values());
    }

    // Méthode pour définir l'événement à modifier
    public void setEvenement(evenement evenement) {
        this.evenement = evenement;
        tfTitre.setText(evenement.getTitre());
        taDescription.setText(evenement.getDescription());
        cbStatut.setValue(evenement.getStatut());
        cbCategorie.setValue(evenement.getCategorie());
        tfPrix.setText(String.valueOf(evenement.getPrix())); // Récupérer le prix
    }

    @FXML
    private void modifierEvenement() {
        // Mettre à jour l'événement avec les nouvelles valeurs
        evenement.setTitre(tfTitre.getText());
        evenement.setDescription(taDescription.getText());

        // Conversion de LocalDate en java.util.Date
        if (dpDate.getValue() != null) {
            evenement.setDate(Date.valueOf(dpDate.getValue()));
        }

        evenement.setStatut(cbStatut.getValue());
        evenement.setCategorie(cbCategorie.getValue());

        // Récupérer et définir le prix
        try {
            double prix = Double.parseDouble(tfPrix.getText());
            evenement.setPrix(prix);
        } catch (NumberFormatException e) {
            // Gérer l'erreur si le prix n'est pas un nombre valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez entrer un prix valide.");
            alert.showAndWait();
            return;
        }

        // Appeler le service pour mettre à jour l'événement
        evenService.modifier(evenement);

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("L'événement a été modifié avec succès !");
        alert.showAndWait();
    }


}
