package com.pi_dev.controllers.EvenementController;
import com.pi_dev.controllers.CommentController.CommentController;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionEvenement.Reservation;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.ReservationService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AfficherEvenementDetails   {

    @FXML
    private ImageView eventImage;

    @FXML
    private Label eventTitle;

    @FXML
    private Label eventDate;

    @FXML
    private Text eventDescription;

    @FXML
    private Label eventPrice;

    @FXML
    private VBox reservationForm;

    @FXML
    private TextField nombrePlaces;

    @FXML
    private Label montantTotal;
    @FXML
    private Button addProgramButton; // Référence au bouton "Ajouter Programme"

    private evenement currentEvenement;
    private Utilisateur utilisateurConnecte; // Utilisateur simulé
    private Utilisateur currentUser;

    // Méthode pour initialiser les données de l'événement
    public void initialize(evenement evenement) {
        AdminService adminService = AdminService.getInstance();
        this.currentUser = adminService.getLoggedUser(); // Fetch logged user from singleton
        this.currentEvenement = evenement;
        if (currentUser != null) {
            // Afficher les informations de l'utilisateur connecté
            System.out.println("Connected User: " + currentUser.getNom());
            System.out.println("User Role: " + currentUser.getRole());

            // Masquer ou afficher le bouton "Ajouter Programme" en fonction du rôle
            if (currentUser.getRole() == Role.ADMIN) {
                addProgramButton.setVisible(true); // Afficher le bouton pour les admins
            } else {
                addProgramButton.setVisible(false); // Masquer le bouton pour les autres rôles
            }
        } else {
            System.out.println("No user is currently connected.");
            addProgramButton.setVisible(false); // Masquer le bouton si aucun utilisateur n'est connecté
        }

        // Afficher les détails de l'événement
        eventTitle.setText(evenement.getTitre());
        eventDate.setText("Date : " + evenement.getDate().toString());
        eventDescription.setText(evenement.getDescription());

        // Afficher le prix de l'événement
        double prix = evenement.getPrix();
        if (prix > 0) {
            eventPrice.setText("Prix : " + prix);
        } else {
            eventPrice.setText("Prix non défini");
        }

        // Charger l'image de l'événement
        if (evenement.getImage() != null && !evenement.getImage().isEmpty()) {
            File imageFile = new File(evenement.getImage());
            if (imageFile.exists()) {
                eventImage.setImage(new Image(imageFile.toURI().toString()));
            } else {
                eventImage.setImage(new Image(evenement.getImage()));  // Essayer comme URL
            }
        }
        // Mettre à jour le montant total lorsque le nombre de places change
        nombrePlaces.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int places = Integer.parseInt(newValue);
                double total = places * currentEvenement.getPrix();
                montantTotal.setText("Montant total : " + total);
            } catch (NumberFormatException e) {
                montantTotal.setText("Montant total : 0.0");
            }
        });
    }

    // Méthode pour afficher une erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher un message de succès
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour réserver l'événement
    @FXML
    public void reserverEvenement(ActionEvent actionEvent) {
        // Faire glisser le formulaire de réservation vers le haut
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), reservationForm);
        transition.setToY(-300); // Déplacer le formulaire de 300 pixels vers le haut
        transition.play();
    }

    // Méthode pour annuler la réservation et cacher le formulaire
    @FXML
    public void cancelReservation(ActionEvent actionEvent) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), reservationForm);
        transition.setToY(0); // Ramener le formulaire à sa position initiale
        transition.play();
    }

    // Méthode pour confirmer la réservation
    @FXML
    public void confirmReservation(ActionEvent actionEvent) {
        // Récupérer l'email de l'utilisateur simulé
        String emailClient = utilisateurConnecte.getEmail();

        // Récupérer le nombre de places
        String nombrePlacesText = nombrePlaces.getText();

        // Valider les champs
        if (nombrePlacesText.isEmpty()) {
            showError("Veuillez entrer le nombre de places.");
            return;
        }

        try {
            // Convertir le nombre de places en entier
            int places = Integer.parseInt(nombrePlacesText);
            if (places <= 0) {
                showError("Le nombre de places doit être supérieur à zéro.");
                return;
            }

            // Créer une réservation
            Reservation reservation = new Reservation(
                    0, // ID (généré automatiquement par la base de données)
                    currentEvenement.getId(), // ID de l'événement
                    emailClient, // Email du client
                    utilisateurConnecte.getId(), // ID de l'utilisateur
                    places, // Nombre de places
                    new Date() // Date de réservation
            );

            // Enregistrer la réservation dans la base de données
            ReservationService reservationService = new ReservationService();
            reservationService.ajouter(reservation);

            // Afficher un message de succès
            showSuccess("Réservation réussie !");

            // Cacher le formulaire après confirmation
            cancelReservation(actionEvent);

        } catch (NumberFormatException e) {
            showError("Le nombre de places doit être un nombre valide.");
        }
    }

    // Méthodes pour naviguer vers d'autres interfaces
    @FXML
    public void addProgramme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AjouterProgramme.fxml"));
            Parent root = loader.load();
            AjouterProgramme controller = loader.getController();
            controller.setEvenementId(this.currentEvenement.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Une erreur s'est produite lors du chargement de la page d'ajout de programme.");
        }
    }

    @FXML
    public void navigateProgramme(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherProgramme.fxml"));
            Parent root = loader.load();
            AfficherProgramme controller = loader.getController();
            controller.initialize(currentEvenement);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Programmes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page des programmes.");
        }
    }

    @FXML
    public void navigateComments(ActionEvent actionEvent) {
        try {
            System.out.println(this.currentEvenement);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceComment/comment.fxml"));
            Parent root = loader.load();
            CommentController controller = loader.getController();
            controller.initialize(this.currentEvenement);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commentaires et Notes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page des commentaires.");
        }
    }

    @FXML
    public void navigateEvents(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) eventImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}