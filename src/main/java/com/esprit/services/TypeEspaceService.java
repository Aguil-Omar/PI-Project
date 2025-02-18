package com.esprit.services;
import com.esprit.models.TypeEspace;
import com.esprit.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class TypeEspaceService implements IService<TypeEspace> {
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public void ajouter(TypeEspace typeEspace) {
        // Insert new type_espace record
        String req = "INSERT INTO type_espace (type, description) VALUES (?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, typeEspace.getType());
            pst.setString(2, typeEspace.getDescription());
            pst.executeUpdate();
            System.out.println("TypeEspace ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifier(TypeEspace typeEspace) {
        // Update type_espace record by id
        String req = "UPDATE type_espace SET type=?, description=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, typeEspace.getType());
            pst.setString(2, typeEspace.getDescription());
            pst.setInt(3, typeEspace.getId());
            pst.executeUpdate();
            System.out.println("TypeEspace modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void supprimer(TypeEspace typeEspace) {
        // Delete type_espace record by id
        String req = "DELETE FROM type_espace WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, typeEspace.getId());
            pst.executeUpdate();
            System.out.println("TypeEspace supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<TypeEspace> rechercher() {
            List<TypeEspace> typeEspaces = new ArrayList<>();
        String req = "SELECT * FROM type_espace";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                TypeEspace typeEspace = new TypeEspace(
                        rs.getInt("id")
                        , rs.getString("type")
                        , rs.getString("description")
                );
                typeEspaces.add(typeEspace);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return typeEspaces;
    }
}