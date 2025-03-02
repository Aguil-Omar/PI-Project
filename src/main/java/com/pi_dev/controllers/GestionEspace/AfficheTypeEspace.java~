package com.pi_dev.controllers.GestionEspace;

import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.services.TypeEspaceService;
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

import java.util.List;

public class AfficheTypeEspace {

    @FXML
    private TableView<TypeEspace> typeTable;
    @FXML
    private TableColumn<TypeEspace, String> cType;
    @FXML
    private TableColumn<TypeEspace, String> cDescription;
    @FXML
    private TableColumn<TypeEspace, Void> cAction;

    private TypeEspaceService typeEspaceService = new TypeEspaceService();

    @FXML
    public void initialize() {
        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        cAction.setCellFactory(createActionCellFactory());
        rafraichirListe();
    }

    @FXML
    public void rafraichirListe() {
        List<TypeEspace> list = typeEspaceService.rechercher();
        System.out.println("Retrieved Data: " + list);  // Debugging

        ObservableList<TypeEspace> typeEspaceObservableList = FXCollections.observableArrayList(list);
        typeTable.setItems(typeEspaceObservableList);
        typeTable.refresh();  // Ensure UI update
    }

    private Callback<TableColumn<TypeEspace, Void>, TableCell<TypeEspace, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    TypeEspace typeEspace = getTableView().getItems().get(getIndex());
                    modifierTypeEspace(typeEspace);
                });
// Delete button action
                deleteButton.setOnAction(event -> {
                    TypeEspace typeEspace = getTableView().getItems().get(getIndex());
                    supprimerTypeEspace(typeEspace); // Delete action
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
    private void modifierTypeEspace(TypeEspace typeEspace) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTypeEspace.fxml"));
            Parent root = loader.load();


            ModifierTypeEspace controller = loader.getController();
            controller.setTypeEspace(typeEspace);


            typeTable.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void supprimerTypeEspace(TypeEspace typeEspace) {
        try {
            typeEspaceService.supprimer(typeEspace); // Call to delete in the service
            rafraichirListe();  // Refresh the table after deletion
            showAlert("Success", "TypeEspace supprimé avec succès !", javafx.scene.control.Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Erreur lors de la suppression du TypeEspace.", javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Show alert method
    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    }

