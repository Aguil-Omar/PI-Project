package com.esprit.controllers;

import com.esprit.models.Utilisateur;
import com.esprit.services.AdminService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class listUtilisateur implements Initializable {

    @FXML
    private TableView<Utilisateur> utilisateurTable;

    @FXML
    private TableColumn<Utilisateur, String> nomColumn;

    @FXML
    private TableColumn<Utilisateur, String> prenomColumn;

    @FXML
    private TableColumn<Utilisateur, String> emailColumn;

    @FXML
    private TableColumn<Utilisateur, String> motDePasseColumn;

    @FXML
    private TableColumn<Utilisateur, String> roleColumn;

    @FXML
    private TableColumn<Utilisateur, String> adresseColumn;

    @FXML
    private TableColumn<Utilisateur, String> telColumn;

    @FXML
    private TableColumn<Utilisateur, String> codePostalColumn;
    @FXML
    private TableColumn<Utilisateur, String> regionColumn;

    @FXML
    private TableColumn<Utilisateur, Void> updateColumn;

    @FXML
    private TableColumn<Utilisateur, Void> deleteColumn;

    private ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();
    private void initData() {
        utilisateurs.clear();
        AdminService adminService = new AdminService();
        List<Utilisateur> utilisateursList = adminService.rechercher();
        codePostalColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Utilisateur, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Utilisateur, String> param) {
                return new SimpleStringProperty(String.valueOf(param.getValue().getAdresse().getCodePostal()));
            }
        });

        regionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Utilisateur, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Utilisateur, String> param) {
                return new SimpleStringProperty(param.getValue().getAdresse().getRegion());
            }
        });
        System.out.println("Utilisateurs list size: " + utilisateursList.size()); // Check if Utilisateur objects are being added to the list
        utilisateurs.addAll(utilisateursList);
        System.out.println("ObservableList size: " + utilisateurs.size()); // Check if Utilisateur objects are being added to the ObservableList
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        motDePasseColumn.setCellValueFactory(new PropertyValueFactory<>("motDePasse"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        codePostalColumn.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        updateColumn.setCellFactory(new Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>>() {
            @Override
            public TableCell<Utilisateur, Void> call(TableColumn<Utilisateur, Void> param) {
                return new TableCell<Utilisateur, Void>() {
                    private final Button updateButton = new Button("Update");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                            updateButton.setOnAction(event -> {
                                // Update utilisateur here
                            });
                            setGraphic(updateButton);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        deleteColumn.setCellFactory(new Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>>() {
            @Override
            public TableCell<Utilisateur, Void> call(TableColumn<Utilisateur, Void> param) {
                return new TableCell<Utilisateur, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                            deleteButton.setOnAction(event -> {
                                AdminService adminService = new AdminService();
                                adminService.supprimer(utilisateur);
                                utilisateurs.remove(utilisateur);
                                utilisateurTable.setItems(utilisateurs);
                            });
                            setGraphic(deleteButton);
                        } else {
                            setGraphic(null);
                        }
                    }
                };


            }
        });
        utilisateurTable.setItems(utilisateurs);
        initData();
    }
}