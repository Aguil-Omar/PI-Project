package com.pi_dev.services;

import com.pi_dev.models.GestionEvenement.Reservation;
import com.pi_dev.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationService {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(Reservation reservation) {
        // Requête SQL corrigée
        String req = "INSERT INTO reservation (id_evenement, email, id_utilisateur, nombre_places, date_reservation) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Associer les valeurs aux paramètres de la requête
            pst.setInt(1, reservation.getEvenementId());  // id_evenement
            pst.setString(2, reservation.getEmailClient()); // email
            pst.setInt(3, reservation.getIdUtilisateur()); // id_utilisateur
            pst.setInt(4, reservation.getNombrePlaces()); // nombre_places
            pst.setDate(5, new java.sql.Date(reservation.getDateReservation().getTime())); // date_reservation

            // Exécuter la requête
            pst.executeUpdate();
            System.out.println("Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }
}