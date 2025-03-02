package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.services.MaterielsServices;
import com.pi_dev.services.TypeEspaceService;
import com.pi_dev.services.TypeMaterielsServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;




public class AjoutMateriels {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrix;

    @FXML
    private ComboBox<String> cmbEtat;
    @FXML
    private ComboBox<TypeMateriels> cmbTypeMateriel;
    private TypeMaterielsServices typeMaterielsService = new TypeMaterielsServices();
    @FXML
    private ImageView ImageV;
    @FXML
    private Button btnAjout;
    @FXML
    private Button btnAnnule ;
    private Disponibilite etat;
    private String imagePath;

    @FXML
    public void initialize() {

        cmbEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
        List<TypeMateriels> typeMateriels = typeMaterielsService.rechercher();
        cmbTypeMateriel.setItems(FXCollections.observableArrayList(typeMateriels));
        cmbTypeMateriel.setCellFactory(param -> new ListCell<TypeMateriels>() {
            @Override
            protected void updateItem(TypeMateriels item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomM());
                }
            }
        });
        cmbTypeMateriel.setButtonCell(new ListCell<TypeMateriels>() {
            @Override
            protected void updateItem(TypeMateriels item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomM());
                }
            }
        });
    }


    @FXML
    void ajout(ActionEvent event) {

        String nom = txtNom.getText();
        String prixStr = txtPrix.getText();
        String etatStr = cmbEtat.getValue();
        TypeMateriels selectedTypeMateriel = cmbTypeMateriel.getSelectionModel().getSelectedItem();
        if (selectedTypeMateriel == null) {
            System.out.println("Please select a TypeMateriels");
            return;
        }

        if (nom.isEmpty() || prixStr.isEmpty() || etatStr == null || selectedTypeMateriel == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            Disponibilte etat = Disponibilte.valueOf(etatStr);

            // Create new Espace object
            Materiels newMateriels = new Materiels(nom, prix, etat, selectedTypeMateriel, imagePath);

            // Add new Espace object to the database
            MaterielsServices MaterielsService = new MaterielsServices();
            MaterielsService.ajouter(newMateriels);

            showAlert("Succès", "Matériel ajouté avec succès !");

            clearFields();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/MainInterface.fxml"));
                Parent root = loader.load();
                txtNom.getScene().setRoot(root);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "L'état du matériel est invalide.");
        }
    }


    @FXML
    private void annuler(ActionEvent event) {
        naviguerVersMain();
    }


    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/MainInterface.fxml"));
            Parent root = loader.load();
            Scene currentScene = btnAjout.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    private void clearFields() {
        txtNom.clear();
        txtPrix.clear();
        cmbEtat.getSelectionModel().clearSelection();
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
            imagePath = selectedFile.getAbsolutePath();
            System.out.println("Image path: " + imagePath);
            Image image = new Image(selectedFile.toURI().toString());
            ImageV.setImage(image);
        }
    }
}
