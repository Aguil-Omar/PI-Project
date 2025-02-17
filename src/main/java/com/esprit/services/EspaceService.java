package com.esprit.services;

import com.esprit.models.Disponibilite;
import com.esprit.models.Espace;
import com.esprit.models.TypeEspace;
import com.esprit.utils.DataSource;
import com.esprit.controllers.AfficheEspace;
import com.esprit.controllers.AjoutEspace;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Espace espace) {
        try {
            String query = "INSERT INTO type_espace (type, description) VALUES (?, ?)";
            PreparedStatement typeStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            typeStmt.setString(1, espace.getTypeEspace().getType());
            typeStmt.setString(2, espace.getTypeEspace().getDescription());
            typeStmt.executeUpdate();

            int typeEspaceId = 0;
            ResultSet rs = typeStmt.getGeneratedKeys();
            if (rs.next()) {
                typeEspaceId = rs.getInt(1);
            }

            String userQuery = "INSERT INTO espace (nom, localisation, etat, type_espace_id) VALUES (?, ?, ?, ?)";
            PreparedStatement espaceStmt = connection.prepareStatement(userQuery);
            espaceStmt.setString(1, espace.getNom());
            espaceStmt.setString(2, espace.getLocalisation());
            espaceStmt.setString(3, espace.getEtat().toString());
            espaceStmt.setInt(4, typeEspaceId);

            espaceStmt.executeUpdate();

            System.out.println("Espace ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<Espace> rechercher() {
        List<Espace> espaces = new ArrayList<>();

        String req = "SELECT e.id, e.nom, e.localisation, e.etat, " +
                "t.id AS type_id, t.type, t.description " +
                "FROM espace e " +
                "JOIN type_espace t ON e.type_espace_id = t.id";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                TypeEspace typeEspace = new TypeEspace(
                        rs.getInt("type_id"),
                        rs.getString("type"),
                        rs.getString("description")
                );

                Espace espace = new Espace(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("localisation"),
                        Disponibilite.valueOf(rs.getString("etat")),
                        typeEspace
                );

                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return espaces;
    }


    @Override
    public void modifier(Espace espace) {
        try {

            String typeEspaceQuery = "UPDATE type_espace SET type = ?, description = ? WHERE id = (SELECT type_espace_id FROM espace WHERE id = ?)";
            PreparedStatement typeStmt = connection.prepareStatement(typeEspaceQuery);
            typeStmt.setString(1, espace.getTypeEspace().getType());
            typeStmt.setString(2, espace.getTypeEspace().getDescription());
            typeStmt.setInt(3, espace.getId());
            typeStmt.executeUpdate();

            // Update Utilisateur
            String espaceQuery = "UPDATE espace SET nom = ?, localisation = ?, etat = ? WHERE id = ?";
            PreparedStatement espaceStmt = connection.prepareStatement(espaceQuery);
            espaceStmt.setString(1, espace.getNom());
            espaceStmt.setString(2, espace.getLocalisation());
            espaceStmt.setString(3, espace.getEtat().toString());
            espaceStmt.setInt(4, espace.getId());
            espaceStmt.executeUpdate();

            System.out.println("Espace modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Espace espace) {
        String req = "DELETE from espace WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, espace.getId());
            pst.executeUpdate();
            System.out.println("Espace supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Espace> getAllEspaces() {
        List<Espace> espaceList = new ArrayList<>();
        String query = "SELECT e.nom, e.localisation, e.etat, t.type, t.description FROM espace e " +
                "JOIN type_espace t ON e.type_espace_id = t.id";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("nom");
                String localisation = rs.getString("localisation");
                String etat = rs.getString("etat");
                String type = rs.getString("type");
                String description = rs.getString("description");

                Espace espace = new Espace(nom, localisation, Disponibilite.valueOf(etat), new TypeEspace(type, description));
                espaceList.add(espace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return espaceList;
    }
    public TypeEspace getTypeEspaceByTypeAndDescription(String type, String description) {
        TypeEspace typeEspace = null;
        String query = "SELECT * FROM type_espace WHERE type = ? AND description = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, type);
            ps.setString(2, description);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String typeFromDb = rs.getString("type");
                String descriptionFromDb = rs.getString("description");

                typeEspace = new TypeEspace(id, typeFromDb, descriptionFromDb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeEspace;
    }
    public void saveTypeEspace(TypeEspace typeEspace) {
        String query = "INSERT INTO type_espace (type, description) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, typeEspace.getType());
            ps.setString(2, typeEspace.getDescription());

            ps.executeUpdate();
            System.out.println("TypeEspace ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Other methods (e.g., ajouter, getTypeEspaceByTypeAndDescription, etc.)
}





