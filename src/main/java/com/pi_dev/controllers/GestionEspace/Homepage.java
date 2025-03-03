package com.pi_dev.controllers.GestionEspace;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Homepage {

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

    public void goespace(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/AfficheEspace.fxml");
    }

    public void gotype(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/AfficheTypeEspace.fxml");
    }

    public void list(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/EspaceList.fxml");
    }
}
