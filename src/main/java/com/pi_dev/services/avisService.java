
package com.pi_dev.services;

import com.pi_dev.models.Forum.avis;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class avisService implements IService<avis> {
    private Connection con = DataSource.getInstance().getConnection();

    public avisService() {
    }
    public avis getAvisByUserAndEvent(int userId, evenement event) {
        String query = "SELECT * FROM avis WHERE utilisateur_id = ? AND evenement_id = ?";
        try {
            PreparedStatement pst = this.con.prepareStatement(query);
            pst.setInt(1, userId);
            pst.setInt(2, event.getId());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new avis(
                        rs.getInt("id"),
                        rs.getInt("note"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("evenement_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void ajouter(avis avis) {
        String req = "INSERT INTO avis (note, date, heur, evenement_id,utilisateur_id) VALUES (?, ?, ?, ?,?)";

        try {

            PreparedStatement pst = this.con.prepareStatement(req);
            pst.setInt(1, avis.getNote());
            pst.setDate(2, avis.getDate());
            pst.setTime(3, avis.getHeur());
            pst.setInt(4, avis.getEvenement_id());
            pst.setInt(5, avis.getUtilisateur_id());
            pst.executeUpdate();
            System.out.println("avis added successfully at " + avis.getHeur());
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }


    public void supprimer(avis avis) {
        String req = "DELETE from avis where id=?";

        try {
            PreparedStatement pst = this.con.prepareStatement(req);
            pst.setInt(1, avis.getId());
            pst.executeUpdate();
            System.out.println("avis deleted successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public void modifier(avis avis) {
        String req = "UPDATE avis set note=?,date=?,heur=? where id=?";

        try {
            PreparedStatement pst = this.con.prepareStatement(req);
            pst.setInt(1, avis.getNote());
            pst.setDate(2, avis.getDate());
            pst.setTime(3, avis.getHeur());
            pst.setInt(4, avis.getId());
            pst.executeUpdate();
            System.out.println("avis updated successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public List<avis> rechercher() {
        List<avis> avisList = new ArrayList();
        String req = "select * from avis";

        try {
            PreparedStatement pst = this.con.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                avisList.add(new avis(rs.getInt("id"), rs.getInt("note"), rs.getDate("date"), rs.getTime("heur")));
            }
        } catch (SQLException var5) {
            SQLException e = var5;
            System.out.println(e.getMessage());
        }

        return avisList;
    }

    public double calculerMoyenneNotes(evenement event) {
        String req = "SELECT AVG(note) as moyenne FROM avis WHERE evenement_id = ?";
        double moyenne = 0.0;

        try {
            PreparedStatement pst = this.con.prepareStatement(req);
            pst.setInt(1, event.getId());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                moyenne = rs.getDouble("moyenne");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la moyenne : " + e.getMessage());
        }

        return moyenne;
    }
}
