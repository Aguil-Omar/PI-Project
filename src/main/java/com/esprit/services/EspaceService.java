package com.esprit.services;

import com.esprit.models.Disponibilite;
import com.esprit.models.Espace;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Espace espace) {
        // Updated query to use type_espace_id
        String req = "INSERT INTO espace (nom, titre, localisation, etat, type_espace_id) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getTitre());
            pst.setString(3, espace.getLocalisation());  // Set Time type
            pst.setString(4, espace.getEtat().toString()); // Enum to String
            pst.setInt(5, espace.getType_espace_id());  // Set foreign key for type_espace
            pst.executeUpdate();
            System.out.println("Espace ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Espace espace) {
        // Updated query to use type_espace_id
        String req = "UPDATE espace SET nom=?, titre=?, localisation=?, etat=?, type_espace_id=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getTitre());
            pst.setString(3, espace.getLocalisation());  // Set Time type
            pst.setString(4, espace.getEtat().toString()); // Enum to String
              // Set foreign key for type_espace
            pst.setInt(6, espace.getId());
            pst.executeUpdate();
            System.out.println("Espace modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Espace espace) {
        String req = "DELETE FROM espace WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, espace.getId());
            pst.executeUpdate();
            System.out.println("Espace supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Espace> rechercher() {
        List<Espace> espaces = new ArrayList<>();

        String req = "SELECT * FROM espace";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Espace espace = new Espace(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("titre"),
                        rs.getString("localisation"),  // Use getTime to match Time type
                        Disponibilite.valueOf(rs.getString("etat")), // Enum from String
                        rs.getInt("type_espace_id") // Get foreign key for type_espace
                );
                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return espaces;
    }
}
