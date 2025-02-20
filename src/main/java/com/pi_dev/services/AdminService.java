package com.pi_dev.services;

import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.IService;
import com.pi_dev.utils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminService implements IService<Utilisateur> {

    private Connection connection = DataSource.getInstance().getConnection();

    /**
     * Ajoute un utilisateur à la base de données.
     *
     * @param utilisateur l'utilisateur à ajouter
     */
    @Override
    public void ajouter(Utilisateur utilisateur) {
        try {
            // Upload the image to the specified location under www
            if (utilisateur.getImageUrl() != null) {
                String sourceFilePath = utilisateur.getImageUrl(); // Source file path
                String destinationDir = "C:\\wamp64\\www\\utilisateur_images"; // Destination directory under www
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
                String imageUrl = "utilisateur_images/" + sourceFile.getName(); // Relative URL for web access
                utilisateur.setImageUrl(imageUrl);
            }

            // Insert Adresse and Utilisateur (your existing code)
            String query = "INSERT INTO Adresse (codePostal, region) VALUES (?, ?)";
            try (PreparedStatement adresseStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                adresseStmt.setInt(1, utilisateur.getAdresse().getCodePostal());
                adresseStmt.setString(2, utilisateur.getAdresse().getRegion());
                adresseStmt.executeUpdate();

                int adresseId = 0;
                try (ResultSet rs = adresseStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        adresseId = rs.getInt(1);
                    }
                }

                String userQuery = "INSERT INTO Utilisateur (nom, prenom, motDePasse, role, adresse_id, tel, email, imageUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                    userStmt.setString(1, utilisateur.getNom());
                    userStmt.setString(2, utilisateur.getPrenom());
                    userStmt.setString(3, utilisateur.getMotDePasse());
                    userStmt.setString(4, utilisateur.getRole().toString());
                    userStmt.setInt(5, adresseId);
                    userStmt.setString(6, utilisateur.getTel());
                    userStmt.setString(7, utilisateur.getEmail());
                    userStmt.setString(8, utilisateur.getImageUrl()); // Set the image URL
                    userStmt.executeUpdate();
                }
            }

            System.out.println("Utilisateur ajouté avec succès.");
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to copy image file: " + e.getMessage());
        }
    }
    public List<Utilisateur> rechercher() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            String query = "SELECT u.id, u.nom, u.prenom, u.motDePasse, u.role, u.tel, u.email, u.imageUrl, " +
                    "a.codePostal, a.region " +
                    "FROM Utilisateur u JOIN Adresse a ON u.adresse_id = a.id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("motDePasse");
                String role = rs.getString("role");
                String tel = rs.getString("tel");
                String imageUrl = rs.getString("imageUrl");
                int codePostal = rs.getInt("codePostal");
                String region = rs.getString("region");

                Adresse adresse = new Adresse(codePostal, region);
                Utilisateur utilisateur = new Utilisateur(id, nom, prenom, email, motDePasse, Role.valueOf(role), adresse, tel, imageUrl);

                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }


    /**
     * Modifier un utilisateur existant.
     *
     * @param utilisateur l'utilisateur à modifier.
     */

    @Override
    public void modifier(Utilisateur utilisateur) {
        PreparedStatement adresseStmt = null;
        PreparedStatement utilisateurStmt = null;
        try {
            // Mettre à jour l'adresse
            String adresseQuery = "UPDATE Adresse SET codePostal = ?, region = ? WHERE id = (SELECT adresse_id FROM Utilisateur WHERE id = ?)";
            adresseStmt = connection.prepareStatement(adresseQuery);
            adresseStmt.setInt(1, utilisateur.getAdresse().getCodePostal());
            adresseStmt.setString(2, utilisateur.getAdresse().getRegion());

            adresseStmt.setInt(3, utilisateur.getId());
            adresseStmt.executeUpdate();

            // Mettre à jour l'utilisateur
            String utilisateurQuery = "UPDATE Utilisateur SET nom = ?, prenom = ?, motDePasse = ?, role = ?, tel = ?, email = ?, imageUrl = ? WHERE id = ?";
            utilisateurStmt = connection.prepareStatement(utilisateurQuery);
            utilisateurStmt.setString(1, utilisateur.getNom());
            utilisateurStmt.setString(2, utilisateur.getPrenom());
            utilisateurStmt.setString(3, utilisateur.getMotDePasse());
            utilisateurStmt.setString(4, utilisateur.getRole().toString());
            utilisateurStmt.setString(5, utilisateur.getTel());
            utilisateurStmt.setString(6, utilisateur.getEmail());

            // Upload the image to the specified location under www
            if (utilisateur.getImageUrl() != null) {
                String sourceFilePath = utilisateur.getImageUrl(); // Source file path
                String destinationDir = "C:\\wamp64\\www\\utilisateur_images"; // Destination directory under www
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
                String imageUrl = "utilisateur_images/" + sourceFile.getName(); // Relative URL for web access
                utilisateur.setImageUrl(imageUrl);
                utilisateurStmt.setString(7, utilisateur.getImageUrl());
            } else {
                utilisateurStmt.setString(7, null);
            }
            utilisateurStmt.setInt(8, utilisateur.getId());

            utilisateurStmt.executeUpdate();

            System.out.println("Utilisateur modifié avec succès.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // Close the PreparedStatements
            try {
                if (adresseStmt != null) adresseStmt.close();
                if (utilisateurStmt != null) utilisateurStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void supprimer(Utilisateur utilisateur) {
        String req = "DELETE from utilisateur WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, utilisateur.getId());
            pst.executeUpdate();
            System.out.println("Utilisateur supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
