package com.pi_dev.services;

import com.pi_dev.models.Forum.avis;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class avisService implements IService<avis> {
    private final Connection con;

    public avisService() {
        try {
            this.con = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur de connexion à la base de données : " + e.getMessage(), e);
        }
    }

    // Récupérer l'avis d'un utilisateur pour un événement spécifique
    public avis getAvisByUserAndEvent(int userId, evenement event) {
        String query = "SELECT * FROM avis WHERE utilisateur_id = ? AND evenement_id = ?";
        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, event.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new avis(
                            rs.getInt("id"),
                            rs.getInt("note"),
                            rs.getInt("utilisateur_id"),
                            rs.getInt("evenement_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void ajouter(avis avis) {
        String req = "INSERT INTO avis (note, date, heur, evenement_id, utilisateur_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = this.con.prepareStatement(req)) {
            pst.setInt(1, avis.getNote());
            pst.setDate(2, avis.getDate());
            pst.setTime(3, avis.getHeur());
            pst.setInt(4, avis.getEvenement_id());
            pst.setInt(5, avis.getUtilisateur_id());
            pst.executeUpdate();
            System.out.println("✅ Avis ajouté avec succès à " + avis.getHeur());
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'avis : " + e.getMessage());
        }
    }

    // Supprimer un avis
    public void supprimer(avis avis) {
        String req = "DELETE FROM avis WHERE id=?";
        try (PreparedStatement pst = this.con.prepareStatement(req)) {
            pst.setInt(1, avis.getId());
            pst.executeUpdate();
            System.out.println("✅ Avis supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'avis : " + e.getMessage());
        }
    }

    // Modifier un avis
    public void modifier(avis avis) {
        String req = "UPDATE avis SET note=?, date=?, heur=? WHERE id=?";
        try (PreparedStatement pst = this.con.prepareStatement(req)) {
            pst.setInt(1, avis.getNote());
            pst.setDate(2, avis.getDate());
            pst.setTime(3, avis.getHeur());
            pst.setInt(4, avis.getId());
            pst.executeUpdate();
            System.out.println("✅ Avis mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de l'avis : " + e.getMessage());
        }
    }

    // Rechercher tous les avis
    public List<avis> rechercher() {
        List<avis> avisList = new ArrayList<>();
        String req = "SELECT * FROM avis";
        try (PreparedStatement pst = this.con.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                avisList.add(new avis(
                        rs.getInt("id"),
                        rs.getInt("note"),
                        rs.getDate("date"),
                        rs.getTime("heur")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des avis : " + e.getMessage());
        }
        return avisList;
    }

    // Calculer la moyenne des notes d'un événement
    public double calculerMoyenneNotes(evenement event) {
        String req = "SELECT AVG(note) as moyenne FROM avis WHERE evenement_id = ?";
        double moyenne = 0.0;
        try (PreparedStatement pst = this.con.prepareStatement(req)) {
            pst.setInt(1, event.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    moyenne = rs.getDouble("moyenne");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du calcul de la moyenne : " + e.getMessage());
        }
        return moyenne;
    }
}
