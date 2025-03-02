package com.pi_dev.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.utils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
        try {
            // Upload the image to the specified location
            if (materiels.getImagePath() != null) {
                String sourceFilePath = materiels.getImagePath(); // Source file path
                String destinationDir = "C:\\wamp64\\www\\images"; // Destination directory
                File sourceFile = new File(sourceFilePath);

                // Check if the source file exists
                if (!sourceFile.exists()) {
                    throw new FileNotFoundException("Image file not found: " + sourceFilePath);
                }

                // Ensure the destination directory exists
                File destinationDirectory = new File(destinationDir);
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdirs(); // Create the directory if it doesn't exist
                }

                // Define the destination file path
                String destinationFilePath = destinationDir + File.separator + sourceFile.getName();
                File destinationFile = new File(destinationFilePath);

                // Copy the file from source to destination
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Set the new image URL for the database
                String imagePath = destinationFile.toURI().toString(); // Use URI to get the absolute path
                materiels.setImagePath(imagePath);
            }

            String req = "INSERT INTO materiel (nom, prix, etat, type_materiel_id, ImagePath) VALUES (?,?,?,?,?)";
            try (PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, materiels.getNom());
                pst.setFloat(2, materiels.getPrix());
                pst.setString(3, materiels.getEtat().toString());

                // Avoid NullPointerException
                if (materiels.getTypeMateriel() != null) {
                    pst.setInt(4, materiels.getTypeMateriel().getId());
                } else {
                    pst.setNull(4, Types.INTEGER);
                }

                pst.setString(5, materiels.getImagePath());
                pst.executeUpdate();

                // Get the generated ID

                System.out.println("✅ Matériel ajouté avec succès !");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to get image file: " + e.getMessage());
        }
    }


    @Override
    public void modifier(Materiels materiels) {
        final String updateQuery = "UPDATE materiel SET nom=?, prix=?, etat=?, type_materiel_id=? , imagePath = ? WHERE id=?";
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
            e.printStackTrace();
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

    public List<Materiels> getMaterielsDisponibles() {
        List<Materiels> materiels = new ArrayList<>();
        final String query = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, tm.nomM, tm.description, m.ImagePath, m.qrCodePath FROM materiel m JOIN type_materiel tm ON m.type_materiel_id = tm.id";

        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet resultSet = pst.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                float prix = resultSet.getFloat("prix");
                String etatStr = resultSet.getString("etat");
                String nomM = resultSet.getString("nomM");
                String description = resultSet.getString("description");
                String imagePath = resultSet.getString("ImagePath");
                String qrCodePath = resultSet.getString("qrCodePath");

                // Convertir String en enum Disponibilte
                Disponibilte etat = Disponibilte.valueOf(etatStr);

                TypeMateriels typeMateriel = new TypeMateriels(resultSet.getInt("type_materiel_id"), nomM, description);
                Materiels materiel = new Materiels(id, nom, prix, etat, typeMateriel, imagePath);

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
        final String query = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, tm.nomM, tm.description, m.ImagePath, m.qrCodePath FROM materiel m JOIN type_materiel tm ON m.type_materiel_id = tm.id";

        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                float prix = rs.getFloat("prix");
                String etatStr = rs.getString("etat");
                String description = rs.getString("description");
                String nomM = rs.getString("nomM");
                String imagePath = rs.getString("ImagePath");
                String qrCodePath = rs.getString("qrCodePath");

                Disponibilte etat = Disponibilte.valueOf(etatStr);  // Conversion en enum
                TypeMateriels typeMateriel = new TypeMateriels(rs.getInt("type_materiel_id"), nomM, description);
                Materiels materiel = new Materiels(id, nom, prix, etat, typeMateriel, imagePath);


                resultats.add(materiel);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de matériels : " + e.getMessage());
        }

        return resultats;
    }
}