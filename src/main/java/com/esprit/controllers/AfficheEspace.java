package com.esprit.controllers;

import com.esprit.models.Espace;
import com.esprit.models.Disponibilite;
import com.esprit.models.TypeEspace;
import com.esprit.services.EspaceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.TableView.*;

public class AfficheEspace implements Initializable {

    @FXML
    private TableView<Espace> espaceTableView;

    @FXML
    private TableColumn<Espace, String> cNom;

    @FXML
    private TableColumn<Espace, String> cLocalisation;

    @FXML
    private TableColumn<Espace, String> cDisponible;

    @FXML
    private TableColumn<Espace, String> cType;

    @FXML
    private TableColumn<Espace, String> cDescription;

    private ObservableList<Espace> espaces = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Displaying the name of the space
        cNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        // Displaying the localisation of the space
        cLocalisation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));

        // Displaying the availability (Etat) using the enum's string representation
        cDisponible.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));

        // Displaying the type of space
        cType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeEspace().getType()));

        // Displaying the description from the TypeEspace object
        cDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeEspace().getDescription()));

        // Load data into table
        espaceTableView.setItems(espaces);
    }

    public void setData(Espace espace) {
        espaces.add(espace);
        espaceTableView.setItems(espaces);  // Ensure the TableView updates with the new data
        espaceTableView.refresh();  // Refresh to reflect the changes in the TableView
    }
    public void refreshTableView() {
        // Get updated list of Espace from the database
        ObservableList<Espace> espaceList = FXCollections.observableArrayList(EspaceService.getAllEspaces());

        // Set the updated data to the TableView
        TableView.setItems(espaceList);
    }
}
