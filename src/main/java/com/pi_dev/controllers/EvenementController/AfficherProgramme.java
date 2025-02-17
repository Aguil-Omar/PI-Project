package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.ProgService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class AfficherProgramme {

    @FXML
    private TableView<programme> tableProgrammes;

    @FXML
    private TableColumn<programme, String> colActivite;

    @FXML
    private TableColumn<programme, String> colHeureDebut;

    @FXML
    private TableColumn<programme, String> colHeureFin;

    @FXML
    private TableColumn<programme, Void> colSupprimer; // Nouvelle colonne pour suppression

    private ProgService progService = new ProgService();
    private ObservableList<programme> programmeList = FXCollections.observableArrayList();

    public void initialize(evenement evenement) {
        // Configuration des colonnes existantes
        colActivite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heurDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heurFin"));


        // Configuration de la colonne "Supprimer"
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    programme prog = getTableView().getItems().get(getIndex());
                    supprimerProgramme(prog);
                });
            }



            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(deleteButton));
                }
            }
        });

        // Charger les programmes et les afficher
        List<programme> programmes = progService.getProgrammesByEvenement(evenement.getId());
        programmeList.setAll(programmes);
        tableProgrammes.setItems(programmeList);
    }

    private void supprimerProgramme(programme prog) {
        // Suppression de la base de données
        progService.supprimer(prog);

        // Mise à jour de la TableView
        programmeList.remove(prog);
    }
}
