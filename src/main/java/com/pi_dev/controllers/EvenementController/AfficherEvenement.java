package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.io.IOException;

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
    private TableColumn<evenement, Void> colAction;

    private EvenService evenService = new EvenService();
    private SuppEvenement suppEvenement = new SuppEvenement(); // Contrôleur pour la suppression

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        // Configuration de la colonne "Action"
        colAction.setCellFactory(column -> new TableCell<evenement, Void>() {
            private final Button detailsButton = new Button("Détails");
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");

            {
                // Action du bouton Détails
                detailsButton.setOnAction(event -> {
                    evenement selectedEvent = getTableView().getItems().get(getIndex());
                    if (selectedEvent != null) {
                        ouvrirPageDetails(selectedEvent);
                    }
                });

                // Action du bouton Supprimer
                deleteButton.setOnAction(event -> {
                    evenement selectedEvent = getTableView().getItems().get(getIndex());
                    if (selectedEvent != null) {
                        if (selectedEvent.getId() > 0) {
                            suppEvenement.supprimerEvenement(selectedEvent);
                            loadEvenements(); // Rafraîchir la table
                        } else {
                            showAlert("Erreur", "ID invalide", AlertType.ERROR);
                        }
                    }
                });

                // Action du bouton Modifier
                editButton.setOnAction(event -> {
                    evenement selectedEvent = getTableView().getItems().get(getIndex());
                    if (selectedEvent != null) {
                        ouvrirPageModification(selectedEvent);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, detailsButton, editButton, deleteButton));
                }
            }
        });

        loadEvenements();
    }

    private void loadEvenements() {
        List<evenement> evenements = evenService.rechercher();
        tableEvenements.getItems().setAll(evenements);
    }

    private void ouvrirPageDetails(evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenementDetails.fxml"));
            Parent root = loader.load();

            AfficherEvenementDetails controller = loader.getController();
            controller.initialize(evenement);

            Stage stage = (Stage) tableEvenements.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'Événement");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des détails de l'événement.", AlertType.ERROR);
        }
    }

    private void ouvrirPageModification(evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/ModifEvenement.fxml"));
            Parent root = loader.load();

            ModifEvenement controller = loader.getController();
            controller.setEvenement(evenement);

            Stage stage = (Stage) tableEvenements.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Événement");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement de la page de modification.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void addEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AjoutEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableEvenements.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ajouter evenement");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement de la page de l'ajout d'évenement.", AlertType.ERROR);
        }
    }
}
