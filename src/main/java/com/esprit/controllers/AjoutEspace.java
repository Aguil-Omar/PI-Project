package com.esprit.controllers;

import com.esprit.models.Disponibilite;
import com.esprit.models.Espace;
import com.esprit.models.TypeEspace;
import com.esprit.services.EspaceService;
import com.esprit.services.TypeEspaceService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class AjoutEspace {
    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfLocalisation;

    @FXML
    private ComboBox<String> cbDisponible;

    @FXML
    private ComboBox<TypeEspace> cbType;

    private final TypeEspaceService typeEspaceService = new TypeEspaceService();
    private Disponibilite etat;

    @FXML
    public void initialize() {
        // Populate ComboBox with ENUM values
        cbDisponible.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));

        // Fetch the list of TypeEspace from the service
        List<TypeEspace> typeEspaces = typeEspaceService.rechercher();
        cbType.setItems(FXCollections.observableArrayList(typeEspaces));

        // Customize the ComboBox to display only the 'type' (not the full object)
        cbType.setCellFactory(param -> new ListCell<TypeEspace>() {
            @Override
            protected void updateItem(TypeEspace item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getType());  // Assuming 'getType()' returns the type name
                }
            }
        });

        cbType.setButtonCell(new ListCell<TypeEspace>() {
            @Override
            protected void updateItem(TypeEspace item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getType());  // Display only the type name in the dropdown
                }
            }
        });
    }

    @FXML
    void AddEspace(ActionEvent event) throws IOException {
        String nom = tfNom.getText();
        String localisation = tfLocalisation.getText();
        String selectedValue = cbDisponible.getValue();
        TypeEspace selectedTypeEspace = cbType.getSelectionModel().getSelectedItem();

        if (selectedTypeEspace == null) {
            // Handle the case where no TypeEspace is selected
            System.out.println("Please select a TypeEspace");
            return;
        }

        // Check if all fields are filled
        if (nom.isEmpty() || localisation.isEmpty() || selectedValue == null || selectedTypeEspace == null) {
            showAlert("Validation Error", "Veuillez remplir tous les champs !", Alert.AlertType.WARNING);
            return;
        }

        // Convert to ENUM safely
        try {
            etat = Disponibilite.valueOf(selectedValue);
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", "Veuillez sélectionner une valeur valide pour la disponibilité !", Alert.AlertType.WARNING);
            return;
        }

        // Create new Espace object
        Espace newEspace = new Espace(nom, localisation, etat, selectedTypeEspace);

        // Add new Espace object to the database
        EspaceService espaceService = new EspaceService();
        espaceService.ajouter(newEspace);

        // Clear the form fields
        tfNom.clear();
        tfLocalisation.clear();
        cbDisponible.getSelectionModel().clearSelection();
        cbType.getSelectionModel().clearSelection();

        // Show success message
        showAlert("Success", "Espace ajouté avec succès !", Alert.AlertType.INFORMATION);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheEspace.fxml"));
        Parent root = loader.load();

        // Set the new scene
        tfNom.getScene().setRoot(root);
    }

    // Show alert method
    private void showAlert(String title, String message, Alert.AlertType warning) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        tfNom.clear();
        tfLocalisation.clear();
        cbDisponible.getSelectionModel().clearSelection();
    }
}
