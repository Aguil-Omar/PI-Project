package com.pi_dev.services;

import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
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

    private final Connection connection;

    public AdminService() {
        this.connection = getConnection();
    }

    // Méthode pour obtenir la connexion à la base de données avec gestion des erreurs
    private Connection getConnection() {
        try {
            Connection conn = DataSource.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Échec de la connexion à la base de données.");
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(Utilisateur utilisateur) {
        try {
            // Gérer l'upload de l'image si elle existe
            if (utilisateur.getImageUrl() != null) {
                utilisateur.setImageUrl(uploadImage(utilisateur.getImageUrl()));
            }

            // Insérer l'adresse
            int adresseId = insertAdresse(utilisateur.getAdresse());

            // Insérer l'utilisateur
            String userQuery = "INSERT INTO Utilisateur (nom, prenom, motDePasse, role, adresse_id, tel, email, imageUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                userStmt.setString(1, utilisateur.getNom());
                userStmt.setString(2, utilisateur.getPrenom());
                userStmt.setString(3, utilisateur.getMotDePasse());
                userStmt.setString(4, utilisateur.getRole().toString());
                userStmt.setInt(5, adresseId);
                userStmt.setString(6, utilisateur.getTel());
                userStmt.setString(7, utilisateur.getEmail());
                userStmt.setString(8, utilisateur.getImageUrl()); // URL de l'image
                userStmt.executeUpdate();
            }

            System.out.println("Utilisateur ajouté avec succès.");
        } catch (FileNotFoundException e) {
            System.err.println("Erreur de fichier : " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Échec de la copie de l'image : " + e.getMessage());
        }
    }

    private String uploadImage(String sourceFilePath) throws IOException {
        String destinationDir = "C:\\wamp64\\www\\utilisateur_images";
        File sourceFile = new File(sourceFilePath);

        if (!sourceFile.exists()) {
            throw new FileNotFoundException("Image non trouvée : " + sourceFilePath);
        }

        File destinationDirectory = new File(destinationDir);
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        String destinationFilePath = destinationDir + File.separator + sourceFile.getName();
        File destinationFile = new File(destinationFilePath);

        // Copier l'image
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return "utilisateur_images/" + sourceFile.getName(); // URL relative pour le web
    }

    private int insertAdresse(Adresse adresse) throws SQLException {
        String adresseQuery = "INSERT INTO Adresse (codePostal, region) VALUES (?, ?)";
        try (PreparedStatement adresseStmt = connection.prepareStatement(adresseQuery, Statement.RETURN_GENERATED_KEYS)) {
            adresseStmt.setInt(1, adresse.getCodePostal());
            adresseStmt.setString(2, adresse.getRegion());
            adresseStmt.executeUpdate();

            try (ResultSet rs = adresseStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public List<Utilisateur> rechercher() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.prenom, u.motDePasse, u.role, u.tel, u.email, u.imageUrl, a.codePostal, a.region " +
                "FROM Utilisateur u JOIN Adresse a ON u.adresse_id = a.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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

    @Override
    public void modifier(Utilisateur utilisateur) {
        try {
            // Mettre à jour l'adresse
            updateAdresse(utilisateur);

            // Mettre à jour l'utilisateur
            updateUtilisateur(utilisateur);

            System.out.println("Utilisateur modifié avec succès.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAdresse(Utilisateur utilisateur) throws SQLException {
        String adresseQuery = "UPDATE Adresse SET codePostal = ?, region = ? WHERE id = (SELECT adresse_id FROM Utilisateur WHERE id = ?)";
        try (PreparedStatement adresseStmt = connection.prepareStatement(adresseQuery)) {
            adresseStmt.setInt(1, utilisateur.getAdresse().getCodePostal());
            adresseStmt.setString(2, utilisateur.getAdresse().getRegion());
            adresseStmt.setInt(3, utilisateur.getId());
            adresseStmt.executeUpdate();
        }
    }

    private void updateUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        String utilisateurQuery = "UPDATE Utilisateur SET nom = ?, prenom = ?, motDePasse = ?, role = ?, tel = ?, email = ?, imageUrl = ? WHERE id = ?";
        try (PreparedStatement utilisateurStmt = connection.prepareStatement(utilisateurQuery)) {
            utilisateurStmt.setString(1, utilisateur.getNom());
            utilisateurStmt.setString(2, utilisateur.getPrenom());
            utilisateurStmt.setString(3, utilisateur.getMotDePasse());
            utilisateurStmt.setString(4, utilisateur.getRole().toString());
            utilisateurStmt.setString(5, utilisateur.getTel());
            utilisateurStmt.setString(6, utilisateur.getEmail());

            if (utilisateur.getImageUrl() != null) {
                utilisateur.setImageUrl(uploadImage(utilisateur.getImageUrl()));
                utilisateurStmt.setString(7, utilisateur.getImageUrl());
            } else {
                utilisateurStmt.setString(7, null);
            }
            utilisateurStmt.setInt(8, utilisateur.getId());

            utilisateurStmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Utilisateur utilisateur) {
        String req = "DELETE from utilisateur WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, utilisateur.getId());
            pst.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
