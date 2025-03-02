package com.Materiels.controllers;

import com.Materiels.models.TypeMateriels;
import com.Materiels.services.TypeMaterielsServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.Scene;

public class AfficheTypeMateriels {

    @FXML
    private TableView<TypeMateriels> tableTypeMateriels;

    @FXML
    private TableColumn<TypeMateriels, String> colNomM;

    @FXML
    private TableColumn<TypeMateriels, String> colDescription;

    @FXML
    private TableColumn<TypeMateriels, Void> colAction;

    @FXML
    public void initialize() {

        colNomM.setCellValueFactory(new PropertyValueFactory<>("nomM"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        colAction.setCellFactory(createActionCellFactory());


        rafraichirListe();
    }

    @FXML
    public void rafraichirListe() {
        TypeMaterielsServices tms = new TypeMaterielsServices();
        ObservableList<TypeMateriels> typeMaterielsList = FXCollections.observableArrayList(tms.rechercher());
        tableTypeMateriels.setItems(typeMaterielsList);
    }


    private Callback<TableColumn<TypeMateriels, Void>, TableCell<TypeMateriels, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {

                editButton.setOnAction(event -> {
                    TypeMateriels typeMateriel = getTableView().getItems().get(getIndex());
                    modifierTypeMateriel(typeMateriel);
                });


                deleteButton.setOnAction(event -> {
                    TypeMateriels typeMateriel = getTableView().getItems().get(getIndex());
                    supprimerTypeMateriel(typeMateriel);
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


    private void modifierTypeMateriel(TypeMateriels typeMateriel) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTypeMateriels.fxml"));
            Parent root = loader.load();


            ModifierTypeMateriels controller = loader.getController();
            controller.setTypeMateriel(typeMateriel);


            tableTypeMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void supprimerTypeMateriel(TypeMateriels typeMateriel) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerTypeMateriels.fxml"));
            Parent root = loader.load();


            SupprimerTypeMateriels controller = loader.getController();
            controller.setTypeMateriel(typeMateriel);


            tableTypeMateriels.getScene().setRoot(root);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutTypeMateriels.fxml"));
            Parent root = loader.load();
            Scene currentScene = tableTypeMateriels.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MainInterface MainController;

    public void setMainController(MainInterface mainController) {
        this.MainController = mainController;
    }


    @FXML
    private void switchToMaterielsTab() {
        if (MainController != null) {
            MainController.switchToMaterielsTab();
        }
    }
}