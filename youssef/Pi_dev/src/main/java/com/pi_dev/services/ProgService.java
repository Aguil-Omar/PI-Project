package com.pi_dev.services;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.time.LocalTime;

public class ProgService implements IService<programme> {
    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(programme programme) {
        String req = "INSERT INTO programme (activite, heurDebut, heurFin, evenement_id) VALUES (?, ?, ?, ?)";
        System.out.println(programme.getEvenementId());
        try {

            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, programme.getActivite());
            pst.setTime(2, java.sql.Time.valueOf(programme.getHeurDebut()));
            pst.setTime(3, java.sql.Time.valueOf(programme.getHeurFin()));
            pst.setInt(4, programme.getEvenementId());

            pst.executeUpdate();
            System.out.println("Programme ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du programme : " + e.getMessage());
        }
    }

    @Override
    public void modifier(programme programme) {
        // Requête SQL pour mettre à jour un programme en utilisant son ID unique
        String req = "UPDATE programme SET activite = ?, heurDebut = ?, heurFin = ?, evenement_id = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Définir les valeurs à mettre à jour
            pst.setString(1, programme.getActivite());
            pst.setTime(2, java.sql.Time.valueOf(programme.getHeurDebut())); // Correction ici
            pst.setTime(3, java.sql.Time.valueOf(programme.getHeurFin()));   // Correction ici
            pst.setInt(4, programme.getEvenementId());
            pst.setInt(5, programme.getId());

            pst.executeUpdate();
            System.out.println("Programme modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(programme programme) {
        String req = "DELETE FROM programme WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, programme.getId());
            pst.executeUpdate();
            System.out.println("Programme supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public List<programme> rechercher() {
        List<programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                programme prog = new programme(
                        rs.getInt("id"),
                        rs.getString("activite"),
                        rs.getTime("heurDebut").toLocalTime(), // Correction ici
                        rs.getTime("heurFin").toLocalTime(),   // Correction ici
                        rs.getInt("evenement_id")
                );

                programmes.add(prog);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des programmes : " + e.getMessage());
        }

        return programmes;
    }
    public List<programme> getProgrammesByEvenement(int evenementId) {
        List<programme> programmes = new ArrayList<>();
        String req = "SELECT * FROM programme WHERE evenement_id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, evenementId);
            ResultSet rs = pst.executeQuery();

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
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des programmes pour l'événement " + evenementId + " : " + e.getMessage());
        }
        return programmes;
    }

}
