package com.pi_dev.controllers.GestionEspace;

import com.pi_dev.models.GestionEsapce.*;
import com.pi_dev.services.EspaceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class AfficheEspace implements Initializable {

    @FXML
    private TableView<Espace> espaceTable;

    @FXML
    private TableColumn<Espace, String> cNom;

    @FXML
    private TableColumn<Espace, String> cLocalisation;


    @FXML
    private TableColumn<Espace, String> cDisponible;
    @FXML
    private TableColumn<Espace, Void> caction;

    private ObservableList<Espace> espaces = FXCollections.observableArrayList();
    private EspaceService espaceService = new EspaceService();  // ✅ Create an instance of EspaceService

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Displaying the name of the space
        cNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        // Displaying the localisation of the space
        cLocalisation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));

        // Displaying the availability (Etat) using the enum's string representation
        cDisponible.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        caction.setCellFactory(createActionCellFactory());

        refreshTableView(); // ✅ Load data from DB when the scene initializes
    }

    public void refreshTableView() {
        ObservableList<Espace> espaceList = FXCollections.observableArrayList(espaceService.rechercher());
        System.out.println("Loaded espaces: " + espaceList);
        espaceTable.setItems(espaceList);
    }

    private Callback<TableColumn<Espace, Void>, TableCell<Espace, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style buttons (optional)
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Espace espace = getTableView().getItems().get(getIndex());
                    modifierEspace(espace);
                });

                deleteButton.setOnAction(event -> {
                    Espace espace = getTableView().getItems().get(getIndex());
                    supprimerEspace(espace);
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
    private void modifierEspace(Espace espace) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEspace.fxml"));
            Parent root = loader.load();

            ModifierEspace controller = loader.getController();
            controller.setEspace(espace);

            espaceTable.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerEspace(Espace espace) {
        try {
            espaceService.supprimer(espace);
            refreshTableView();
            System.out.println("Espace supprimé: " + espace.getNom());
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }
}
