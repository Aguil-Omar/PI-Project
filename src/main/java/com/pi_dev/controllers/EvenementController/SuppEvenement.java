package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.EvenService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SuppEvenement {

    private EvenService evenService = new EvenService();


    public void supprimerEvenement(evenement evenement) {
        if (evenement != null) {
            System.out.println("ID de l'événement à supprimer : " + evenement.getId());  // Vérifie l'ID

            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'événement");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'événement de la base de données
                evenService.supprimer(evenement);
                // Afficher une confirmation après la suppression
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'événement a été supprimé avec succès.");
                successAlert.showAndWait();
            }
        } else {
            System.out.println("Aucun événement sélectionné.");
        }
    }


}