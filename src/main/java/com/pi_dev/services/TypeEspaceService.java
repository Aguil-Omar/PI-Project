package com.pi_dev.services;

import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeEspaceService implements IService<TypeEspace> {

    private Connection connection;

    // ✅ Gestion sécurisée de la connexion
    public TypeEspaceService() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(TypeEspace typeEspace) {
        String req = "INSERT INTO type_espace (type, description) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, typeEspace.getType());
            pst.setString(2, typeEspace.getDescription());
            pst.executeUpdate();
            System.out.println("✅ TypeEspace ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du TypeEspace : " + e.getMessage());
        }
    }

    @Override
    public void modifier(TypeEspace typeEspace) {
        String req = "UPDATE type_espace SET type = ?, description = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, typeEspace.getType());
            pst.setString(2, typeEspace.getDescription());
            pst.setInt(3, typeEspace.getId());
            pst.executeUpdate();
            System.out.println("✅ TypeEspace modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du TypeEspace : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(TypeEspace typeEspace) {
        String req = "DELETE FROM type_espace WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, typeEspace.getId());
            pst.executeUpdate();
            System.out.println("✅ TypeEspace supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du TypeEspace : " + e.getMessage());
        }
    }

    @Override
    public List<TypeEspace> rechercher() {
        List<TypeEspace> typeEspaces = new ArrayList<>();
        String req = "SELECT * FROM type_espace";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                TypeEspace typeEspace = new TypeEspace(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("description")
                );
                typeEspaces.add(typeEspace);
            }
            System.out.println("✅ Recherche des TypeEspace terminée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche des TypeEspace : " + e.getMessage());
        }
        return typeEspaces;
    }
}
