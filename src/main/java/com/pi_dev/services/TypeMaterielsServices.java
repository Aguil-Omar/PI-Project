package com.pi_dev.services;

import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeMaterielsServices implements IService<TypeMateriels> {

    private Connection connection;

    // ✅ Gestion de l'exception SQLException
    public TypeMaterielsServices() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(TypeMateriels materiels) {
        String req = "INSERT INTO type_materiel (nomM, description) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, materiels.getNomM());
            pst.setString(2, materiels.getDescription());
            pst.executeUpdate();
            System.out.println("✅ Type de matériel ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du type de matériel : " + e.getMessage());
        }
    }

    @Override
    public void modifier(TypeMateriels materiels) {
        String req = "UPDATE type_materiel SET nomM = ?, description = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, materiels.getNomM());
            pst.setString(2, materiels.getDescription());
            pst.setInt(3, materiels.getId());
            pst.executeUpdate();
            System.out.println("✅ Type de matériel modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du type de matériel : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(TypeMateriels materiels) {
        String req = "DELETE FROM type_materiel WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, materiels.getId());
            pst.executeUpdate();
            System.out.println("✅ Type de matériel supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du type de matériel : " + e.getMessage());
        }
    }

    @Override
    public List<TypeMateriels> rechercher() {
        List<TypeMateriels> listeTypeMateriels = new ArrayList<>();
        String req = "SELECT * FROM type_materiel";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                TypeMateriels typeMateriels = new TypeMateriels(
                        rs.getInt("id"),
                        rs.getString("nomM"), // 🔹 Suppression du préfixe "type_materiel."
                        rs.getString("description")
                );
                listeTypeMateriels.add(typeMateriels);
            }
            System.out.println("✅ Recherche des types de matériels terminée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche des types de matériels : " + e.getMessage());
        }
        return listeTypeMateriels;
    }
}
