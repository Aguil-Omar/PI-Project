package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.services.MaterielsServices;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class AfficheMateriels {

    @FXML
    private TableView<Materiels> tableMateriels;

    @FXML
    private TableColumn<Materiels, String> colNom;

    @FXML
    private TableColumn<Materiels, Float> colPrix;

    @FXML
    private TableColumn<Materiels, String> colEtat;

    @FXML
    private TableColumn<Materiels, String> colType;

    @FXML
    private TableColumn<Materiels, Void> colAction;


    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de Materiels
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeMateriel().getNomM()));

        // Configuration de la colonne d'actions
        colAction.setCellFactory(createActionCellFactory());

        // Rafraîchir la liste des matériels
        rafraichirListe();
    }

    @FXML
    public void rafraichirListe() {
        MaterielsServices ms = new MaterielsServices();
        ObservableList<Materiels> materielsList = FXCollections.observableArrayList(ms.rechercher());
        System.out.println("Nombre de matériels récupérés : " + materielsList.size());
        tableMateriels.setItems(materielsList);
    }

    private Callback<TableColumn<Materiels, Void>, TableCell<Materiels, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Action pour le bouton "Modifier"
                editButton.setOnAction(event -> {
                    Materiels materiel = getTableView().getItems().get(getIndex());
                    modifierMateriel(materiel);
                });

                // Action pour le bouton "Supprimer"
                deleteButton.setOnAction(event -> {
                    Materiels materiel = getTableView().getItems().get(getIndex());
                    supprimerMateriel(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        };
    }

    private void modifierMateriel(Materiels materiel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/ModifierMateriels.fxml"));
            Parent root = loader.load();

            ModifierMateriels controller = loader.getController();
            controller.setMateriel(materiel);

            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue de modification.");
        }
    }

    private void supprimerMateriel(Materiels materiel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/SupprimerMateriels.fxml"));
            Parent root = loader.load();

            SupprimerMateriel controller = loader.getController();
            controller.setMateriel(materiel);

            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue de suppression.");
        }
    }

    @FXML
    private void Ajout() {
        naviguerVersAjout();
    }

    private void naviguerVersAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/AjoutMateriels.fxml"));
            Parent root = loader.load();
            Scene currentScene = tableMateriels.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue d'ajout.");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private MainInterface mainController;

    public void setMainController(MainInterface mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void switchToTypeMaterielsTab() {
        if (mainController != null) {
            mainController.switchToTypeMaterielsTab();
        }
    }
}