package com.pi_dev.services;

import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.Reservation;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierServices {

    private final Connection connection;

    public PanierServices() {
        this.connection = DataSource.getInstance().getConnection();
    }

    // Méthode pour ajouter une réservation de matériel avec l'ID de l'événement
    public void ajouter(Reservation reservation, Utilisateur utilisateur, int idEvenement) {
        String query = "INSERT INTO Panier (utilisateur, date_debut, date_fin, materiel_id, id_evenement) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, utilisateur.getId());  // ID de l'utilisateur
            pst.setDate(2, new java.sql.Date(reservation.getDateDebut().getTime()));  // Date de début
            pst.setDate(3, new java.sql.Date(reservation.getDateFin().getTime()));  // Date de fin
            pst.setInt(4, reservation.getMateriel().getId());  // ID du matériel
            pst.setInt(5, idEvenement);  // ID de l'événement
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de l'ajout de la réservation au panier : " + e.getMessage(), e);
        }
    }

    // Méthode pour récupérer les réservations d'un utilisateur
    public List<Reservation> getReservations(Utilisateur utilisateur) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT p.id AS panier_id, p.utilisateur, p.date_debut, p.date_fin, " +
                "m.id AS materiel_id, m.nom, m.prix, m.etat, m.ImagePath, " +
                "u.id AS utilisateur_id, u.nom AS utilisateur_nom, u.email AS utilisateur_email " +
                "FROM panier p " +
                "JOIN materiel m ON p.materiel_id = m.id " +
                "JOIN utilisateur u ON p.utilisateur = u.id " +
                "WHERE p.utilisateur = ?";  // Filtrage par utilisateur

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, utilisateur.getId());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Création de l'objet Utilisateur
                    Utilisateur utilisateurResult = new Utilisateur(
                            rs.getInt("utilisateur_id"),
                            rs.getString("utilisateur_nom"),
                            rs.getString("utilisateur_email")
                    );

                    // Création de l'objet Materiels
                    Materiels materiel = new Materiels(
                            rs.getInt("materiel_id"),
                            rs.getString("nom"),
                            rs.getFloat("prix"),
                            Disponibilte.valueOf(rs.getString("etat")),
                            null, // TypeMateriels est null pour l'instant
                            rs.getString("ImagePath")
                    );

                    // Création de l'objet Reservation
                    Reservation reservation = new Reservation(
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin"),
                            materiel,
                            utilisateurResult  // Ajout de l'utilisateur
                    );
                    reservation.setId(rs.getInt("panier_id"));
                    reservations.add(reservation);
                }
            }
            System.out.println(reservations);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return reservations;
    }

    // Méthode pour récupérer les réservations de matériels pour un événement donné
    public List<Reservation> getReservationsParEvenement(int idEvenement) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT p.id AS panier_id, p.utilisateur, p.date_debut, p.date_fin, " +
                "m.id AS materiel_id, m.nom, m.prix, m.etat, m.ImagePath, " +
                "u.id AS utilisateur_id, u.nom AS utilisateur_nom, u.email AS utilisateur_email " +
                "FROM panier p " +
                "JOIN materiel m ON p.materiel_id = m.id " +
                "JOIN utilisateur u ON p.utilisateur = u.id " +
                "WHERE p.id_evenement = ?";  // Filtrage par événement

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idEvenement);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Création de l'objet Utilisateur
                    Utilisateur utilisateurResult = new Utilisateur(
                            rs.getInt("utilisateur_id"),
                            rs.getString("utilisateur_nom"),
                            rs.getString("utilisateur_email")
                    );

                    // Création de l'objet Materiels
                    Materiels materiel = new Materiels(
                            rs.getInt("materiel_id"),
                            rs.getString("nom"),
                            rs.getFloat("prix"),
                            Disponibilte.valueOf(rs.getString("etat")),
                            null, // TypeMateriels est null pour l'instant
                            rs.getString("ImagePath")
                    );

                    // Création de l'objet Reservation
                    Reservation reservation = new Reservation(
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin"),
                            materiel,
                            utilisateurResult  // Ajout de l'utilisateur
                    );
                    reservation.setId(rs.getInt("panier_id"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return reservations;
    }

    // Méthode pour supprimer une réservation
    public void supprimer(Reservation reservation) {
        String query = "DELETE FROM Panier WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reservation.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la suppression de la réservation du panier : " + e.getMessage(), e);
        }
    }
    public List<String> getMaterielsReservesPourEvenement(int idEvenement) {
        List<String> materielsReserves = new ArrayList<>();
        String query = "SELECT m.nom FROM materiel m " +
                "JOIN panier p ON m.id = p.materiel_id " +
                "WHERE p.id_evenement = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idEvenement);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                materielsReserves.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des matériels réservés : " + e.getMessage());
        }
        return materielsReserves;
    }
    public List<String> getUtilisateursPourEvenement(int idEvenement) {
        List<String> utilisateurs = new ArrayList<>();
        String query = "SELECT u.nom FROM utilisateur u " +
                "JOIN panier p ON u.id = p.utilisateur " +
                "WHERE p.id_evenement = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idEvenement);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                utilisateurs.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }
}