package com.pi_dev.controllers.GestionEspace;

import com.pi_dev.models.GestionEsapce.*;
import com.pi_dev.services.EspaceService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierEspace implements Initializable {

    @FXML
    private TextField mnom;

    @FXML
    private TextField mlocalisation;

    @FXML
    private ComboBox<Disponibilite> metat;

    @FXML
    private Button modifierButton;

    private Espace espace;  // Object to modify
    private EspaceService espaceService = new EspaceService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the ComboBox with enum values
        metat.setItems(FXCollections.observableArrayList(Disponibilite.values()));
    }

    // Method to set the selected espace before modifying
    public void setEspace(Espace espace) {
        this.espace = espace;
        if (espace != null) {
            mnom.setText(espace.getNom());
            mlocalisation.setText(espace.getLocalisation());
            metat.setValue(espace.getEtat());  // Set the selected value in ComboBox
        }
    }

    @FXML
    private void modifierespace(ActionEvent event)throws IOException {
        if (espace != null) {
            espace.setNom(mnom.getText());
            espace.setLocalisation(mlocalisation.getText());
            espace.setEtat(metat.getValue());  // Get selected value from ComboBox


                espaceService.modifier(espace);  // Save changes
                System.out.println("Espace modified successfully!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEspace/AfficheEspace.fxml"));
                Parent root = loader.load();
                mlocalisation.getScene().setRoot(root);



            // Set the new scene

        }
    }
}
