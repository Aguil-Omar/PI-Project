package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AfficheEspace {

    @FXML
    private Label lbNom;

    @FXML
    private Label lbTitre;

    @FXML
    private Label lbLocalisation;

    @FXML
    private Label lbDisponible;

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
}
