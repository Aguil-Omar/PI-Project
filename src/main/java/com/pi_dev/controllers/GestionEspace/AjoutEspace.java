package com.pi_dev.controllers.GestionEspace;

import com.pi_dev.models.GestionEsapce.*;
import com.pi_dev.services.EspaceService;
import com.pi_dev.services.TypeEspaceService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    @FXML
    private ImageView imageV;

    private final TypeEspaceService typeEspaceService = new TypeEspaceService();
    private Disponibilite etat;
    private String imageUrl;

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
        if (nom.isEmpty() || localisation.isEmpty() || selectedValue == null || selectedTypeEspace == null || imageUrl == null) {
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
        Espace newEspace = new Espace(nom, localisation, etat, selectedTypeEspace, imageUrl);

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEspace/AfficheEspace.fxml"));
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
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retour(ActionEvent actionEvent)  {
        switchScene(actionEvent, "/InterfaceEspace/AfficheEspace.fxml");


    }


    @FXML
    private void upload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers d'image", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imageUrl = selectedFile.getAbsolutePath();
            System.out.println("Image path: " + imageUrl);
            Image image = new Image(selectedFile.toURI().toString());
            imageV.setImage(image);
        }
    }
}
