package com.pi_dev.services;

import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielsServices implements IService<Materiels> {

    private final Connection connection;

    public MaterielsServices() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(Materiels materiels) {
        final String req = "INSERT INTO materiel (nom, prix, etat, type_materiel_id, ImagePath) VALUES (?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, materiels.getNom());
            pst.setFloat(2, materiels.getPrix());
            pst.setString(3, materiels.getEtat().toString());
            pst.setInt(4, materiels.getTypeMateriel().getId());
            pst.setString(5, materiels.getImagePath());

            pst.executeUpdate();
            System.out.println("✅ Matériel ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du matériel : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Materiels materiels) {
        final String updateQuery = "UPDATE materiel SET nom=?, prix=?, etat=?, type_materiel_id=? , imagePath = ?WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
            pst.setString(1, materiels.getNom());
            pst.setFloat(2, materiels.getPrix());
            pst.setString(3, materiels.getEtat().toString());
            pst.setInt(4, materiels.getTypeMateriel().getId());
            pst.setString(5, materiels.getImagePath());
            pst.setInt(6, materiels.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Matériel modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun matériel trouvé avec l'ID : " + materiels.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la modification du matériel : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Materiels materiels) {
        final String req = "DELETE FROM materiel WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, materiels.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Matériel supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun matériel trouvé avec l'ID : " + materiels.getId());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du matériel : " + e.getMessage());
        }
    }

    private ObservableList<Materiels> getMaterielsDisponiblesDepuisBD() {
        ObservableList<Materiels> materiels = FXCollections.observableArrayList();
        final String query = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, tm.nomM, tm.description, m.ImagePath FROM materiel m JOIN type_materiel tm ON m.type_materiel_id = tm.id";

        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet resultSet = pst.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                float prix = resultSet.getFloat("prix");
                String etatStr = resultSet.getString("etat");
                String nomM = resultSet.getString("nomM");
                String description = resultSet.getString("description");
                String imagePath = resultSet.getString("ImagePath");

                // Convertir String en enum Disponibilte
                Disponibilte etat = Disponibilte.valueOf(etatStr);

                TypeMateriels typeMateriel = new TypeMateriels(nomM, description);
                Materiels materiel = new Materiels(nom, prix, etat, typeMateriel, imagePath);
                materiels.add(materiel);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des matériels depuis la base de données.");
        }
        return materiels;
    }

    @Override
    public List<Materiels> rechercher() {
        List<Materiels> resultats = new ArrayList<>();
        final String query = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, tm.nomM, tm.description, m.ImagePath FROM materiel m JOIN type_materiel tm ON m.type_materiel_id = tm.id";

        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                float prix = rs.getFloat("prix");
                String etatStr = rs.getString("etat");
                String description = rs.getString("description");
                String nomM = rs.getString("nomM");
                String imagePath = rs.getString("ImagePath");

                Disponibilte etat = Disponibilte.valueOf(etatStr);  // Conversion en enum
                TypeMateriels typeMateriel = new TypeMateriels(nomM, description);
                Materiels materiel = new Materiels(nom, prix, etat, typeMateriel, imagePath);

                resultats.add(materiel);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de matériels : " + e.getMessage());
        }

        return resultats;
    }
}
