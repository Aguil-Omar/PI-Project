package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Labeled;

public class AfficheEspace {

    @FXML
    private Label lbNom;

    @FXML
    private Label lbTitre;

    @FXML
    private Label lbLocalisation;

    @FXML
    private Label lbDisponible;

    @FXML
    private Label lbType;

    @FXML
    private Label lbDescription;


    public void setLbNom(String nom) {
        this.lbNom.setText(nom);
    }

    public void setLbTitre(String titre) {
        this.lbTitre.setText(titre);
    }

    public void setLbLocalisation(String localisation) {
        this.lbLocalisation.setText(localisation);
    }

    public void setLbDisponible(String disponible) {
        this.lbDisponible.setText(disponible);
    }
    public void setLbType(String type) {
        this.lbType.setText(type);

    }

    public void setLbDescription(String description) {
        this.lbDescription.setText(description);
    }


}
