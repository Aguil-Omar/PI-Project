package com.pi_dev.controllers;

import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AfficherEvenement {

    @FXML
    private TableView<evenement> tableEvenements;

    @FXML
    private TableColumn<evenement, String> colTitre;
    @FXML
    private TableColumn<evenement, String> colDescription;
    @FXML
    private TableColumn<evenement, String> colDate;
    @FXML
    private TableColumn<evenement, String> colStatut;
    @FXML
    private TableColumn<evenement, String> colCategorie;
    @FXML
    private TableColumn<evenement, Void> colAction; // Colonne pour les actions

    private EvenService evenService = new EvenService();
    private SuppEvenement suppEvenement = new SuppEvenement(); // Contrôleur pour la suppression

    @FXML
    public void initialize() {
        // Initialisation des colonnes existantes
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        // Configuration de la colonne "Action"
        colAction.setCellFactory(column -> new TableCell<evenement, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    evenement selectedEvent = getTableView().getItems().get(getIndex());  // Récupérer l'événement sélectionné
                    if (selectedEvent != null) {
                        System.out.println("ID de l'événement sélectionné : " + selectedEvent.getId()); // Vérifie l'ID ici
                        // Vérifie que l'ID est valide
                        if (selectedEvent.getId() > 0) {
                            suppEvenement.supprimerEvenement(selectedEvent);
                            loadEvenements(); // Rafraîchir la table
                        } else {
                            System.out.println("ID invalide : " + selectedEvent.getId());
                        }
                    }
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Masquer le bouton si la ligne est vide
                } else {
                    setGraphic(deleteButton); // Afficher le bouton "Supprimer"
                }
            }
        });

        // Charger les événements dans la TableView
        loadEvenements();
    }

    // Méthode pour charger les événements dans le TableView
    private void loadEvenements() {
        List<evenement> evenements = evenService.rechercher();  // Récupérer les événements depuis la base
        tableEvenements.getItems().setAll(evenements);  // Afficher les événements dans le tableau
    }
}