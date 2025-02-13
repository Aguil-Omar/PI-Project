package com.pi_dev.services;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class ProgService implements IService<programme> {
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public void ajouter(programme programme) {
        String req = "INSERT INTO programme (activite, heurDebut, heurFin, evenement_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, programme.getActivite());
            pst.setString(2, programme.getHeurDebut());
            pst.setString(3, programme.getHeurFin());
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
            // Préparer la requête
            PreparedStatement pst = connection.prepareStatement(req);

            // Définir les valeurs à mettre à jour
            pst.setString(1, programme.getActivite()); // Nouvelle activité
            pst.setString(2, programme.getHeurDebut()); // Nouvelle heure de début
            pst.setString(3, programme.getHeurFin());   // Nouvelle heure de fin
            pst.setInt(4, programme.getEvenementId());  // Nouvel ID d'événement
            pst.setInt(5, programme.getId());           // ID du programme à modifier

            // Exécuter la requête
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
            // Préparation de la requête pour supprimer le programme en utilisant son ID
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, programme.getId());  // On passe l'ID du programme à supprimer
            pst.executeUpdate();  // Exécution de la requête de suppression
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
                        rs.getInt("id"),            // ID du programme
                        rs.getString("activite"),    // Activité du programme
                        rs.getString("heurDebut"),   // Heure de début
                        rs.getString("heurFin"),     // Heure de fin
                        rs.getInt("evenement_id")    // ID de l'événement associé
                );

                programmes.add(prog);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des programmes : " + e.getMessage());
        }

        return programmes;
    }





}
