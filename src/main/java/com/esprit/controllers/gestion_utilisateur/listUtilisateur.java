package com.esprit.controllers.gestion_utilisateur;

import  com.esprit.models.utilisateur.Utilisateur;
import com.esprit.services.utilisateur.AdminService;
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

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    @FXML
    private ImageView userIcon;

    @FXML
    private Label loggedUserName;
    private Utilisateur utilisateur;
@FXML
private ImageView logoutIcon;
    private ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList();
private Utilisateur loggedUser;
    public void initData() {
        utilisateurs.clear();
        AdminService adminService = AdminService.getInstance();
        this.loggedUser = adminService.getLoggedUser(); // Fetch logged user from singleton
        System.out.println(loggedUser);// Ensure singleton usage
        List<Utilisateur> utilisateursList = adminService.rechercher();
        utilisateurs.addAll(utilisateursList);
        // Set logged-in user's name
        loggedUserName.setText(loggedUser.getNom() + " " + loggedUser.getPrenom());
        System.out.println(loggedUserName.getText());
        // Load Profile Picture
        if (loggedUser.getImageUrl() != null && !loggedUser.getImageUrl().isEmpty()) {
            String imagePath = "file:/C:/wamp64/www/" + loggedUser.getImageUrl();
            userIcon.setImage(new Image(imagePath));
        } else {
            userIcon.setImage(new Image("file:/C:/wamp64/www/utilisateur_images/nullimage.png")); // Default profile icon
        }
    }
    @FXML
    private void goToModifyProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyProfile.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userIcon.getScene().getWindow();
            profilControlleur pc=loader.getController();
            pc.setUtilisateur(loggedUser);
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                                imageView.setImage(new Image("C:\\wamp64\\www\\utilisateur_images\\nullimage.png"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutUtilisateur.fxml"));
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
    @FXML
    public void logout() {
        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to log out or Cancel to stay logged in.");

        // Wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Perform logout actions
            System.out.println("User logged out successfully.");

            // Close the current window
            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.close();

            // Open login window (Assuming Login.fxml exists)
            openLoginWindow();
        }
    }


    private void openLoginWindow() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/signin.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new javafx.scene.Scene(root));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void filterUtilisateurs(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            utilisateurTable.setItems(utilisateurs);
        } else {
            ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();
            for (Utilisateur utilisateur : utilisateurs) {
                if (utilisateur.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        utilisateur.getPrenom().toLowerCase().contains(searchText.toLowerCase()) ||
                        utilisateur.getEmail().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(utilisateur);
                }
            }
            utilisateurTable.setItems(filteredList);
        }
    }
}