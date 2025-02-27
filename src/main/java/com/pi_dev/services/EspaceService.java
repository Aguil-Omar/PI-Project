package com.pi_dev.services;

import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private final Connection connection;

    public EspaceService() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(Espace espace) {
        final String req = "INSERT INTO espace (nom, localisation, etat, type_espace_id) VALUES (?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getLocalisation());
            pst.setString(3, espace.getEtat().toString());  // Enum to String
            pst.setInt(4, espace.getTypeEspace().getId());  // Assuming TypeEspace has getId()

            pst.executeUpdate();
            System.out.println("✅ Espace ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'espace : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Espace espace) {
        final String req = "UPDATE espace SET nom=?, localisation=?, etat=?, type_espace_id=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getLocalisation());
            pst.setString(3, espace.getEtat().toString());  // Enum to String
            pst.setInt(4, espace.getTypeEspace().getId());  // Assuming TypeEspace has getId()
            pst.setInt(5, espace.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Espace modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun espace trouvé avec l'ID : " + espace.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification de l'espace : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Espace espace) {
        final String req = "DELETE FROM espace WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, espace.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Espace supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun espace trouvé avec l'ID : " + espace.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'espace : " + e.getMessage());
        }
    }

    @Override
    public List<Espace> rechercher() {
        List<Espace> espaces = new ArrayList<>();
        final String req = "SELECT * FROM espace";

        try (PreparedStatement pst = connection.prepareStatement(req); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                // Convertir String en Enum pour etat
                Disponibilite etat = Disponibilite.valueOf(rs.getString("etat"));
                // Créer un objet TypeEspace avec la clé étrangère
                TypeEspace typeEspace = new TypeEspace(rs.getInt("type_espace_id"));

                // Créer un objet Espace et l'ajouter à la liste
                Espace espace = new Espace(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("localisation"),
                        etat,  // Définir l'enum Disponibilite
                        typeEspace  // Définir l'objet TypeEspace
                );
                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des espaces : " + e.getMessage());
        }

        return espaces;  // Retourner la liste des objets Espace
    }
}
