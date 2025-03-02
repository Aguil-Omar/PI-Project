package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.services.MaterielsServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class ReservationController {

    @FXML
    private TilePane tilePane;

    private MaterielsServices materielsServices;

    public ReservationController() {
        this.materielsServices = new MaterielsServices();
    }

    @FXML
    private void initialize() {
        afficherMaterielsDisponibles();
    }

    private void afficherMaterielsDisponibles() {
        List<Materiels> materielsDisponibles = materielsServices.getMaterielsDisponibles();
        for (Materiels materiel : materielsDisponibles) {
            VBox materielCard = new VBox(10);
            materielCard.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
            ImageView materielImage = new ImageView();
            String imagePath = materiel.getImagePath();

            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File("src/main/resources/images/" + new File(imagePath).getName());
                if (imageFile.exists()) {
                    materielImage.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    materielImage.setImage(new Image("file:src/main/resources/images/nullimage.png"));
                }
            } else {
                materielImage.setImage(new Image("file:src/main/resources/images/nullimage.png"));
            }
            materielImage.setFitWidth(100);
            materielImage.setFitHeight(100);
            Label materielNom = new Label(materiel.getNom());
            Label materielPrix = new Label("Prix: " + materiel.getPrix());
            Label materielEtat = new Label("État: " + materiel.getEtat());
            materielCard.getChildren().addAll(materielImage, materielNom, materielPrix, materielEtat);
            tilePane.getChildren().add(materielCard);
        }
    }
}