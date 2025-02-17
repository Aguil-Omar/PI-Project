package com.Materiels.controllers;

import com.Materiels.models.Materiels;
import com.Materiels.services.MaterielsServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.Scene;

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
    private TableColumn<Materiels, Void> colAction;

    @FXML
    public void initialize() {

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));


        colAction.setCellFactory(createActionCellFactory());


        rafraichirListe();
    }


    @FXML
    public void rafraichirListe() {
        MaterielsServices ms = new MaterielsServices();
        ObservableList<Materiels> materielsList = FXCollections.observableArrayList(ms.rechercher());
        tableMateriels.setItems(materielsList);

    }


    private Callback<TableColumn<Materiels, Void>, TableCell<Materiels, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {

                editButton.setOnAction(event -> {
                    Materiels materiel = getTableView().getItems().get(getIndex());
                    modifierMateriel(materiel);
                });


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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierMateriels.fxml"));
            Parent root = loader.load();


           ModifierMateriels controller = loader.getController();
            controller.setMateriel(materiel);


            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void supprimerMateriel(Materiels materiel) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerMateriels.fxml"));
            Parent root = loader.load();

            SupprimerMateriel controller = loader.getController();
            controller.setMateriel(materiel);


            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @FXML
    private void Ajout() {
        naviguerVersAjout();
    }

    private void naviguerVersAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutMateriels.fxml"));
            Parent root = loader.load();
            Scene currentScene = tableMateriels.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
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





