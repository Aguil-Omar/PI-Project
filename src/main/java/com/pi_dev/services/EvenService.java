package com.pi_dev.services;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenService implements IService<evenement> {
    private final Connection connection;

    public EvenService() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(evenement evenement) {
        final String req = "INSERT INTO evenement (titre, description, date, statut, categorie) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setDate(3, new java.sql.Date(evenement.getDate().getTime()));
            pst.setString(4, evenement.getStatut());
            pst.setString(5, evenement.getCategorie().name());

            pst.executeUpdate();
            System.out.println("✅ Événement ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void modifier(evenement evenement) {
        final String req = "UPDATE evenement SET titre = ?, description = ?, date = ?, statut = ?, categorie = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setDate(3, new java.sql.Date(evenement.getDate().getTime()));
            pst.setString(4, evenement.getStatut());
            pst.setString(5, evenement.getCategorie().name());
            pst.setInt(6, evenement.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Événement modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun événement trouvé avec l'ID : " + evenement.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(evenement evenement) {
        final String req = "DELETE FROM evenement WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, evenement.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Événement supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun événement trouvé avec l'ID : " + evenement.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    @Override
    public List<evenement> rechercher() {
        List<evenement> evenements = new ArrayList<>();
        final String req = "SELECT * FROM evenement";

        try (PreparedStatement pst = connection.prepareStatement(req); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                evenement evt = new evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        rs.getString("statut"),
                        Categorie.valueOf(rs.getString("categorie"))
                );

                evenements.add(evt);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des événements : " + e.getMessage());
        }

        return evenements;
    }
}
