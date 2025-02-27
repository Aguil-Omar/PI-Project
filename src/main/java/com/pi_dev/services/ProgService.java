package com.pi_dev.services;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgService implements IService<programme> {

    private final Connection connection;

    // ✅ Constructeur sécurisé pour la connexion
    public ProgService() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(programme programme) {
        final String req = "INSERT INTO programme (activite, heurDebut, heurFin, evenement_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, programme.getActivite());
            pst.setTime(2, Time.valueOf(programme.getHeurDebut()));
            pst.setTime(3, Time.valueOf(programme.getHeurFin()));
            pst.setInt(4, programme.getEvenementId());

            pst.executeUpdate();
            System.out.println("✅ Programme ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du programme : " + e.getMessage());
        }
    }

    @Override
    public void modifier(programme programme) {
        final String req = "UPDATE programme SET activite = ?, heurDebut = ?, heurFin = ?, evenement_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, programme.getActivite());
            pst.setTime(2, Time.valueOf(programme.getHeurDebut()));
            pst.setTime(3, Time.valueOf(programme.getHeurFin()));
            pst.setInt(4, programme.getEvenementId());
            pst.setInt(5, programme.getId());

            int updatedRows = pst.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("✅ Programme modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun programme trouvé avec l'ID : " + programme.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du programme : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(programme programme) {
        final String req = "DELETE FROM programme WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, programme.getId());

            int deletedRows = pst.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("✅ Programme supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun programme trouvé avec l'ID : " + programme.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du programme : " + e.getMessage());
        }
    }

    @Override
    public List<programme> rechercher() {
        List<programme> programmes = new ArrayList<>();
        final String req = "SELECT * FROM programme";

        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                programme prog = new programme(
                        rs.getInt("id"),
                        rs.getString("activite"),
                        rs.getTime("heurDebut").toLocalTime(),
                        rs.getTime("heurFin").toLocalTime(),
                        rs.getInt("evenement_id")
                );
                programmes.add(prog);
            }
            System.out.println("✅ Recherche des programmes terminée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des programmes : " + e.getMessage());
        }
        return programmes;
    }

    public List<programme> getProgrammesByEvenement(int evenementId) {
        List<programme> programmes = new ArrayList<>();
        final String req = "SELECT * FROM programme WHERE evenement_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, evenementId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    programme prog = new programme(
                            rs.getInt("id"),
                            rs.getString("activite"),
                            rs.getTime("heurDebut").toLocalTime(),
                            rs.getTime("heurFin").toLocalTime(),
                            rs.getInt("evenement_id")
                    );
                    programmes.add(prog);
                }
            }
            System.out.println("✅ Programmes récupérés pour l'événement " + evenementId);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des programmes pour l'événement " + evenementId + " : " + e.getMessage());
        }
        return programmes;
    }
}
