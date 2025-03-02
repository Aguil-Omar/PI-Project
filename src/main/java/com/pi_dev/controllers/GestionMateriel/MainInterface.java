package com.pi_dev.controllers.GestionMateriel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainInterface {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab materielsTab;

    @FXML
    private Tab typeMaterielsTab;


    @FXML
    public void initialize() {
        try {

            FXMLLoader materielsLoader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/AfficheMateriels.fxml"));
            AnchorPane materielsContent = materielsLoader.load();
            materielsTab.setContent(materielsContent);
            AfficheMateriels materielsController = materielsLoader.getController();
            materielsController.setMainController(this);


            FXMLLoader typeMaterielsLoader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/AfficheTypeMateriels.fxml"));
            AnchorPane typeMaterielsContent = typeMaterielsLoader.load();
            typeMaterielsTab.setContent(typeMaterielsContent);
            AfficheTypeMateriels typeMaterielsController = typeMaterielsLoader.getController();
            typeMaterielsController.setMainController(this);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des interfaces dans les onglets.");
        }
    }


    public void switchToMaterielsTab() {
        tabPane.getSelectionModel().select(materielsTab);
    }



}
