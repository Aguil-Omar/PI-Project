package com.pi_dev.controllers.gestion_utilisateur;

import  com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class listUtilisateur implements Initializable {

    @FXML
    private TableView<Utilisateur> utilisateurTable;

    @FXML
    private TextField searchField;

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
    private TableColumn<Utilisateur, String> imageColumn;

    @FXML
    private TableColumn<Utilisateur, Void> updateColumn;

    @FXML
    private TableColumn<Utilisateur, Void> deleteColumn;

    private ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();

    private void initData() {
        utilisateurs.clear();
        AdminService adminService = new AdminService();
        List<Utilisateur> utilisateursList = adminService.rechercher();

        utilisateurs.addAll(utilisateursList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomColumn.setSortable(true);

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenomColumn.setSortable(true);

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setSortable(true);

        motDePasseColumn.setCellValueFactory(new PropertyValueFactory<>("motDePasse"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
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

        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        imageColumn.setCellFactory(new Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>>() {
            @Override
            public TableCell<Utilisateur, String> call(TableColumn<Utilisateur, String> param) {
                return new TableCell<Utilisateur, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (!empty) {
                            if (imageUrl == null || imageUrl.isEmpty()) {
                                imageView.setImage(new Image("C:\\wamp64\\www\\utilisateur_images\\nullimage.jpg"));
                            } else {
                                imageView.setImage(new Image("C:\\wamp64\\www\\" + imageUrl));
                            }
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

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
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateUtilisateur.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                updateUtilisateur controller = loader.getController();
                                controller.setUtilisateur(utilisateur);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.show();
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

        // Ajouter un écouteur sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUtilisateurs(newValue);
        });

        // Initialisation des données
        initData();
        utilisateurTable.setItems(utilisateurs);
    }
    @FXML
    public void handleAddUtilisateur() {
        try {
            // Load the ajoutUtilisateur.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceUtilisateur/ajoutUtilisateur.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new scene
            Stage stage = new Stage();
            stage.setTitle("Add Utilisateur");

            // Set the scene and show the stage
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterUtilisateurs(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            utilisateurTable.setItems(utilisateurs);
        } else {
            ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();
            for (Utilisateur utilisateur : utilisateurs) {
                String nom = utilisateur.getNom() != null ? utilisateur.getNom().toLowerCase() : "";
                String prenom = utilisateur.getPrenom() != null ? utilisateur.getPrenom().toLowerCase() : "";
                String email = utilisateur.getEmail() != null ? utilisateur.getEmail().toLowerCase() : "";

                if (nom.contains(searchText.toLowerCase()) ||
                        prenom.contains(searchText.toLowerCase()) ||
                        email.contains(searchText.toLowerCase())) {
                    filteredList.add(utilisateur);
                }
            }
            utilisateurTable.setItems(filteredList);
        }
    }
}