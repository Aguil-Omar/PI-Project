package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.ProgService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherProgramme    {

    @FXML
    private TableView<programme> tableProgrammes;

    @FXML
    private TableColumn<programme, String> colActivite;

    @FXML
    private TableColumn<programme, String> colHeureDebut;

    @FXML
    private TableColumn<programme, String> colHeureFin;

    @FXML
    private TableColumn<programme, Void> colActions; // Colonne pour les boutons "Modifier" et "Supprimer"

    private ProgService progService = new ProgService();
    private ObservableList<programme> programmeList = FXCollections.observableArrayList();
    private Utilisateur currentUser;

    public void initialize(evenement evenement) {




        // Configuration des colonnes
        colActivite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        colHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heurDebut"));
        colHeureFin.setCellValueFactory(new PropertyValueFactory<>("heurFin"));

        // Configuration des boutons Modifier et Supprimer
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button modifieButton = new Button("Modifier");
            private final HBox buttonContainer = new HBox(10); // Espacement de 10px entre les boutons

            {
                // Style du bouton "Supprimer"
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    programme prog = getTableView().getItems().get(getIndex());
                    supprimerProgramme(prog);
                });

                // Style du bouton "Modifier"
                modifieButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                modifieButton.setOnAction(event -> {
                    programme prog = getTableView().getItems().get(getIndex());
                    modifierProgramme(prog);
                });

                // Ajout des boutons dans le HBox
                buttonContainer.getChildren().addAll(modifieButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonContainer);
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

    private void modifierProgramme(programme prog) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/ModifProgramme.fxml"));
            Parent root = loader.load();

            // Passer le programme à la page de modification
            ModifProgramme controller = loader.getController();
            controller.initData(prog);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/AfficherEvenementDetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableProgrammes.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
