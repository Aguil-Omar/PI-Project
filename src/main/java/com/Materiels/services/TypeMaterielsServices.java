package com.Materiels.services;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.models.TypeMateriels;
import com.Materiels.utils.DataSources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeMaterielsServices implements Iservives<TypeMateriels>{


        private Connection connection = DataSources.getInstance().getConnection();

        @Override
        public void ajouter(TypeMateriels Materiels) {
            String req = "INSERT INTO type_materiel (nomM, description) VALUES (?,?)";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNomM());
                pst.setString(2, Materiels.getDescription());



                pst.executeUpdate();
                System.out.println("type de materiel ajoutée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void modifier(TypeMateriels Materiels) {
            String req = "UPDATE type_materiel  SET nomM=? ,description=? WHERE id=?";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setString(1, Materiels.getNomM());
                pst.setString(2, Materiels.getDescription());
                pst.setInt(3, Materiels.getId());


                pst.executeUpdate();
                System.out.println("type de materiel modifiée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    @Override
        public void supprimer(TypeMateriels Materiels) {
            String req = "DELETE from type_materiel WHERE id=?";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                pst.setInt(1, Materiels.getId());
                pst.executeUpdate();
                System.out.println("type de materiel supprimée");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public List<TypeMateriels> rechercher() {
            List<TypeMateriels> TypeMateriels = new ArrayList<>();

            String req = "SELECT * FROM type_materiel";
            try {
                PreparedStatement pst = connection.prepareStatement(req);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    TypeMateriels.add(new TypeMateriels(
                            rs.getInt("id"),
                            rs.getString("type_materiel.nomM"),
                            rs.getString("type_materiel.description")

                    ));

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            return TypeMateriels;
        }
    }




