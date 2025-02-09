
package com.esprit.services;

import com.esprit.models.avis;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class avisService implements Iservice<avis> {
    private Connection con = DataSource.getInstance().getConnection();

    public avisService() {
    }

    public void ajouter(avis avis) {
        String req = "INSERT into avis (note,date,heur) values (?,?,?)";

        try {
            PreparedStatement pst = this.con.prepareStatement(req);
            pst.setInt(1, avis.getNote());
            pst.setDate(2, avis.getDate());
            pst.setTime(3, avis.getHeur());
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
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public List<avis> afficher() {
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
}
