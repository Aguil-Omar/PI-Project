package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.AdminReservationRow;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.EvenService;
import com.pi_dev.services.PanierServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminReservationsController implements Initializable {

    @FXML
    private TableView<AdminReservationRow> reservationsTable;

    private EvenService evenService = new EvenService();
    private PanierServices panierServices = new PanierServices();
    private Utilisateur currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AdminService adminService = AdminService.getInstance();
        this.currentUser = adminService.getLoggedUser(); // Fetch logged user from singleton

        // Récupérer les données et les afficher dans le tableau
        afficherReservations();
    }

    private void afficherReservations() {
        // Récupérer tous les événements
        List<evenement> evenements = evenService.rechercher();

        // Créer une liste pour les lignes du tableau
        ObservableList<AdminReservationRow> rows = FXCollections.observableArrayList();

        // Pour chaque événement, récupérer les réservations (espace, matériels, utilisateurs)
        for (evenement evenement : evenements) {
            String titreEvenement = evenement.getTitre();

            // Récupérer l'espace réservé pour cet événement
            String espaceReserve = evenement.getEspace() != null ? evenement.getEspace().getNom() : "Aucun espace réservé";

            // Récupérer les matériels réservés pour cet événement
            List<String> materielsReserves = getMaterielsReservesPourEvenement(evenement.getId());

            // Récupérer les utilisateurs qui ont réservé pour cet événement
            List<String> utilisateurs = getUtilisateursPourEvenement(evenement.getId());

            // Créer une ligne pour le tableau
            AdminReservationRow row = new AdminReservationRow(titreEvenement, espaceReserve, materielsReserves, utilisateurs);
            rows.add(row);
        }

        // Afficher les données dans le tableau
        reservationsTable.setItems(rows);
    }
    private List<String> getMaterielsReservesPourEvenement(int idEvenement) {
        // Récupérer les matériels réservés depuis la base de données
        return panierServices.getMaterielsReservesPourEvenement(idEvenement);
    }

    private List<String> getUtilisateursPourEvenement(int idEvenement) {
        // Récupérer les utilisateurs qui ont réservé depuis la base de données
        return panierServices.getUtilisateursPourEvenement(idEvenement);
    }
}