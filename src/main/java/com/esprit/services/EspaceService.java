package com.esprit.services;

import com.esprit.models.Disponibilite;
import com.esprit.models.categorieEspace;
import com.esprit.models.Espace;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Espace espace) {
        String req = "INSERT INTO espace (nom, titre, localisation, etat, categorie) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getTitre());
            pst.setString(3, espace.getLocalisation());
            pst.setString(4, espace.getetat().toString()); // Enum to String
            pst.setString(5, espace.getCategorie().toString()); // Enum to String
            pst.executeUpdate();
            System.out.println("Espace ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Espace espace) {
        String req = "UPDATE espace SET nom=?, titre=?, localisation=?, etat=?, categorie=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getTitre());
            pst.setString(3, espace.getLocalisation());
            pst.setString(4, espace.getetat().toString()); // Enum to String
            pst.setString(5, espace.getCategorie().toString()); // Enum to String
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
                        rs.getString("localisation"),
                        Disponibilite.valueOf(rs.getString("etat")), // Enum from String
                        categorieEspace.valueOf(rs.getString("categorie")) // Enum from String
                );
                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return espaces;
    }
}
