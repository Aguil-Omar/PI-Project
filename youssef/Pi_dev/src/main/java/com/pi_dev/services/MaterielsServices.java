package com.pi_dev.services;

import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterielsServices implements IService<Materiels> {
   
        private Connection connection = DataSource.getInstance().getConnection();

        @Override
        public void ajouter(Materiels Materiels) {
            String req = "INSERT INTO materiel (nom, prix,etat, type_materiel_id ) VALUES (?,?,?,?)";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNom());
                pst.setFloat(2, Materiels.getPrix());
                pst.setString(3, Materiels.getEtat().toString());
                pst.setInt(4, Materiels.getTypeMateriel().getId());


                pst.executeUpdate();
                System.out.println("materiel ajoutée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void modifier(Materiels Materiels) {
            String req = "UPDATE materiel  SET nom=? ,prix=?,etat=?, type_materiel_id =? WHERE id=? ";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNom());
                pst.setFloat(2, Materiels.getPrix());
                pst.setString(3, Materiels.getEtat().toString());
                pst.setInt(4, Materiels.getTypeMateriel ().getId());
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

            String req = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, tm.nomM, tm.description FROM materiel m JOIN type_materiel tm ON m.type_materiel_id = tm.id";

            System.out.println(req);

            try {
                PreparedStatement pst = connection.prepareStatement(req);
                ResultSet rs = pst.executeQuery();
                System.out.println(rs);
                while (rs.next()) {

                    TypeMateriels typeMateriel = new TypeMateriels(
                            rs.getInt("id"),
                            rs.getString("nomM"),
                            rs.getString("description")
                    );
                    Materiels materiel = new Materiels(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getFloat("prix"),
                            Disponibilte.valueOf(rs.getString("etat")),
                            typeMateriel

                    );
                    materiels.add(materiel);

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            return materiels;

        }
    }


