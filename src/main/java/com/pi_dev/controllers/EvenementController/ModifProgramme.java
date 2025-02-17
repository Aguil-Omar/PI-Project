/*package com.pi_dev.controllers.EvenementController;
import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.services.ProgService;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;


public class ModifProgramme {

    @FXML
    private TextField tfActivite;

    @FXML
    private TextField tfHeureDebut;

    @FXML
    private TextField tfHeureFin;

    private int evenementId;
    private programme programme;
    private ProgService progService = new ProgService();


    // Méthode pour définir l'événement à modifier
    public void setProgramme(programme programme) {
        this.programme = programme;
        tfActivite.setText(programme.getActivite());
        tfHeureDebut.setText(programme.getHeurDebut().toString());
        tfHeureFin.setText(programme.getHeurFin().toString());
    }
    @FXML
    private void modifierEvenement() {
        programme.setActivite(tfActivite.getText());
        programme.setHeurDebut(tfHeureFin.getText().toString());

        // Conversion de LocalDate en java.util.Date
        if (dpDate.getValue() != null) {
            evenement.setDate(Date.valueOf(dpDate.getValue()));
        }

        evenement.setStatut(cbStatut.getValue());
        evenement.setCategorie(cbCategorie.getValue());

        // Appeler le service pour mettre à jour l'événement
        evenService.modifier(evenement);

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("L'événement a été modifié avec succès !");
        alert.showAndWait();
    }







}
*/