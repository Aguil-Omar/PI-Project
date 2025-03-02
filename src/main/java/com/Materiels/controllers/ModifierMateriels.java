package com.Materiels.controllers;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.models.TypeMateriels;
import com.Materiels.services.MaterielsServices;
import com.Materiels.services.TypeMaterielsServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;

import java.util.List;


public class ModifierMateriels{

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrix;
    @FXML
    private ComboBox<String> cmbEtat;
    @FXML
    private ComboBox<TypeMateriels> cmbTypeMateriel;
    private TypeMaterielsServices typeMaterielsService = new TypeMaterielsServices();

    private Materiels materiel;

    @FXML
    public void initialize() {

        cmbEtat.setItems(FXCollections.observableArrayList("DISPONIBLE", "INDISPONIBLE"));
        List<TypeMateriels> typeMateriels = typeMaterielsService.rechercher();
        cmbTypeMateriel.setItems(FXCollections.observableArrayList(typeMateriels));
        cmbTypeMateriel.setCellFactory(param -> new ListCell<TypeMateriels>() {
            @Override
            protected void updateItem(TypeMateriels item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNomM()); // Affiche uniquement le nom
            }
        });

        cmbTypeMateriel.setButtonCell(new ListCell<TypeMateriels>() {
            @Override
            protected void updateItem(TypeMateriels item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNomM()); // Affiche uniquement le nom
            }
        });

    }






    public void setMateriel(Materiels materiel) {
        this.materiel = materiel;
        txtNom.setText(materiel.getNom());
        txtPrix.setText(String.valueOf(materiel.getPrix()));
        cmbEtat.setValue(materiel.getEtat().toString());
        cmbTypeMateriel.setValue(materiel.getTypeMateriel());

    }



    @FXML
    private void annuler() {
        naviguerVersMain();
    }


    private void naviguerVersMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainInterface.fxml"));
            Parent root = loader.load();
            txtNom.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface d'affichage.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public void update(ActionEvent event) {
        String nom = txtNom.getText();
        String prixStr = txtPrix.getText();
        String etatStr = cmbEtat.getValue();
        TypeMateriels typeMateriel =cmbTypeMateriel.getValue();

        if (nom.isEmpty() || prixStr.isEmpty() || etatStr == null || typeMateriel == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            Disponibilte etat = Disponibilte.valueOf(etatStr);



            materiel.setNom(nom);
            materiel.setPrix(prix);
            materiel.setEtat(etat);
            materiel.setTypeMateriel(typeMateriel);

            System.out.println(materiel);
            MaterielsServices ms = new MaterielsServices();
            ms.modifier(materiel);

            naviguerVersMain();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "L'état du matériel est invalide.");
        }
    }

}