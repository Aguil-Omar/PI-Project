package com.Materiels.services;

import com.Materiels.models.CategorieMateriel;
import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.utils.DataSources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterielsServices implements Iservives<Materiels> {
   
        private Connection connection = DataSources.getInstance().getConnection();

        @Override
        public void ajouter(Materiels Materiels) {
            String req = "INSERT INTO materiel (nom, prix,etat,categorie) VALUES (?,?,?,?)";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNom());
                pst.setFloat(2, Materiels.getPrix());
                pst.setString(3, Materiels.getEtat().toString());
                pst.setString(4, Materiels.getCategorie().toString());

                pst.executeUpdate();
                System.out.println("materiel ajoutée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void modifier(Materiels Materiels) {
            String req = "UPDATE materiel SET nom=? ,prix=?,etat=?,categorie=? WHERE id=?";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNom());
                pst.setFloat(3, Materiels.getPrix());
                pst.setString(4, Materiels.getEtat().toString());
                pst.setString(2, Materiels.getCategorie().toString());
                pst.setInt(5, Materiels.getId());
                pst.executeUpdate();
                System.out.println("materiel modifiée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void supprimer(Materiels Materiels) {
            String req = "DELETE from materiel WHERE id=?";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setInt(1, Materiels.getId());
                pst.executeUpdate();
                System.out.println("materiel supprimée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public List<Materiels> rechercher() {
            List<Materiels> materiels = new ArrayList<>();

            String req = "SELECT * FROM materiel";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                ResultSet rs = pst.executeQuery(req);
                while (rs.next()) {
                    materiels.add(new Materiels(rs.getInt("id"), rs.getString("nom"),
                            rs.getFloat("prix"), Disponibilte.valueOf(rs.getString("etat")) ,
                            CategorieMateriel.valueOf(rs.getString("categorie"))));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            return materiels;
        }
    }


