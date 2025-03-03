package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.Reservation;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.MaterielsServices;
import com.pi_dev.services.PanierServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ReservationController {

    @FXML
    private TilePane tilePane;
    @FXML
    private TextField utilisateurField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;

    private MaterielsServices materielsServices;
    private PanierServices panierServices;
    private Utilisateur loggedUser;
    private int evenementId; // ID de l'événement associé

    // Méthode pour définir l'ID de l'événement
    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    public ReservationController() {
        this.materielsServices = new MaterielsServices();
        this.panierServices = new PanierServices();
    }

    @FXML
    private void initialize() {
        AdminService adminService = AdminService.getInstance();
        this.loggedUser = adminService.getLoggedUser(); // Récupérer l'utilisateur connecté

        afficherMaterielsDisponibles();
    }

    private void afficherMaterielsDisponibles() {
        List<Materiels> materielsDisponibles = materielsServices.getMaterielsDisponibles();
        for (Materiels materiel : materielsDisponibles) {
            VBox materielCard = new VBox(10);
            materielCard.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
            ImageView materielImage = new ImageView();
            String imagePath = materiel.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File("src/main/resources/" + imagePath);
                if (imageFile.exists()) {
                    materielImage.setImage(new Image(imageFile.toURI().toString()));
                    System.out.println("Image loaded: " + imagePath);
                } else {
                    System.err.println("Image file not found: " + imagePath);
                    materielImage.setImage(new Image("file:src/main/resources/images/"));
                }
            } else {
                // Image par défaut si le chemin est null ou vide
                materielImage.setImage(new Image("file:src/main/resources/images/"));
                System.out.println("Default image loaded for: " + materiel.getNom());
            }
            materielImage.setFitHeight(100);
            materielImage.setFitWidth(100);
            Label materielNom = new Label(materiel.getNom());
            Label materielPrix = new Label("Prix: " + materiel.getPrix());
            Label materielEtat = new Label("État: " + materiel.getEtat());
            Button ajouterAuPanierBtn = new Button("Ajouter au panier");
            ajouterAuPanierBtn.setOnAction(event -> handleAddToCart(materiel));
            materielCard.getChildren().addAll(materielImage, materielNom, materielPrix, materielEtat, ajouterAuPanierBtn);
            tilePane.getChildren().add(materielCard);
        }
    }

    private void handleAddToCart(Materiels materiel) {
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        // Vérifier que les champs sont remplis
        if (dateDebut == null || dateFin == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Vérifier que l'ID de l'événement est valide
        if (evenementId <= 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("L'événement associé n'est pas valide.");
            alert.showAndWait();
            return;
        }

        // Créer une réservation
        Reservation reservation = new Reservation(
                Date.from(dateDebut.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()),
                Date.from(dateFin.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()),
                materiel,
                loggedUser
        );

        // Ajouter la réservation au panier avec l'ID de l'événement
        panierServices.ajouter(reservation, loggedUser, evenementId);

        // Afficher un message de succès
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(materiel.getNom() + " a été ajouté au panier.");
        alert.showAndWait();
    }

    @FXML
    private void handleViewCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/PanierMateriels.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Panier des Matériels");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}