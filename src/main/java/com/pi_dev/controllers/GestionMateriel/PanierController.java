package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Reservation;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.PanierServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class PanierController {

    @FXML
    private TableView<Reservation> panierTable;
    @FXML
    private TableColumn<Reservation, String> nomColumn;
    @FXML
    private TableColumn<Reservation, Float> prixColumn;
    @FXML
    private TableColumn<Reservation, String> dateDebutColumn;
    @FXML
    private TableColumn<Reservation, String> dateFinColumn;
    @FXML
    private TableColumn<Reservation, String> utilisateurColumn;
    @FXML
    private TableColumn<Reservation, Button> actionsColumn;

    private PanierServices panierServices;
    private Utilisateur loggedUser;

    public PanierController() {
        this.panierServices = new PanierServices();
    }

    @FXML
    private void initialize() {
        AdminService adminService = AdminService.getInstance();
        this.loggedUser = adminService.getLoggedUser();
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        utilisateurColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        afficherPanier();


    }

    private void afficherPanier() {
        List<Reservation> panier = panierServices.getReservations(loggedUser);
        System.out.println(panier);
        for (Reservation reservation : panier) {
            Button supprimerBtn = new Button("Supprimer");
            supprimerBtn.setOnAction(event -> handleSupprimer(reservation));
            reservation.setActions(supprimerBtn);
        }
        panierTable.getItems().setAll(panier);
    }

    private void handleSupprimer(Reservation reservation) {
        panierServices.supprimer(reservation);
        afficherPanier();
    }

    @FXML
    private void handleValider() {
        if (panierTable.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Le panier est vide.");
            alert.showAndWait();
            return;
        }

        // Logic for validating the reservation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Réservation validée avec succès.");
        alert.showAndWait();

        // Close the panier window after validation
        Stage stage = (Stage) panierTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleRetour() {
        // Close the panier window and return to the reservation window
        Stage stage = (Stage) panierTable.getScene().getWindow();
        stage.close();
    }
}