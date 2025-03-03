package com.pi_dev.services;

import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.services.IService;
import com.pi_dev.utils.DataSource;
import org.mindrot.jbcrypt.BCrypt;

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
    private Utilisateur loggedUser;
    private static AdminService instance;
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
                String hashedPassword = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt());

                String userQuery = "INSERT INTO Utilisateur (nom, prenom, motDePasse, role, adresse_id, tel, email, imageUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                    userStmt.setString(1, utilisateur.getNom());
                    userStmt.setString(2, utilisateur.getPrenom());
                    userStmt.setString(3, hashedPassword);
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
            String hashedPassword = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt());

            // Mettre à jour l'utilisateur
            String utilisateurQuery = "UPDATE Utilisateur SET nom = ?, prenom = ?, motDePasse = ?, role = ?, tel = ?, email = ?, imageUrl = ? WHERE id = ?";
            utilisateurStmt = connection.prepareStatement(utilisateurQuery);
            utilisateurStmt.setString(1, utilisateur.getNom());
            utilisateurStmt.setString(2, utilisateur.getPrenom());
            utilisateurStmt.setString(3, hashedPassword);
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
    public Utilisateur authentifier(String email, String motDePasse) {
        try {
            // Check if email exists
            String query = "SELECT * FROM Utilisateur WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("Email non trouvé.");
                    return null; // Email does not exist
                }
                String hashedPassword = BCrypt.hashpw(motDePasse, BCrypt.gensalt());

                // Verify password
                String storedPassword = rs.getString("motDePasse");
                if (storedPassword.equals(hashedPassword)) {
                    System.out.println("Mot de passe incorrect.");
                    return null; // Incorrect password
                }

                // Construct the Utilisateur object
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String role = rs.getString("role");
                String tel = rs.getString("tel");
                String imageUrl = rs.getString("imageUrl");

                // Retrieve address details
                int adresseId = rs.getInt("adresse_id");
                Adresse adresse = getAdresseById(adresseId);

                Utilisateur utilisateur = new Utilisateur(id, nom, prenom, email, motDePasse, Role.valueOf(role), adresse, tel, imageUrl);
                System.out.println("Authentification réussie !");
                loggedUser=utilisateur;
                AdminService.getInstance().setLoggedUser(loggedUser);
                System.out.println(loggedUser);
                return loggedUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
    public Utilisateur getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Utilisateur user) {
        this.loggedUser = user;
    }
    private Adresse getAdresseById(int adresseId) {
        try {
            String query = "SELECT * FROM Adresse WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, adresseId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Adresse(rs.getInt("codePostal"), rs.getString("region"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean userExists(String email) {
        try {

                String query = "SELECT COUNT(*) FROM Utilisateur WHERE email = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // If count > 0, user exists

                }
                return false;
            // User does not exist
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    public Utilisateur getUserByEmail(String email) throws SQLException {
        // Define the SQL query to fetch user details along with address information
        String query = "SELECT u.id, u.nom, u.prenom, u.motDePasse, u.role, u.tel, u.email, u.imageUrl, " +
                "a.codePostal, a.region " +
                "FROM Utilisateur u JOIN Adresse a ON u.adresse_id = a.id WHERE u.email = ?";

        // Use try-with-resources to ensure the PreparedStatement and ResultSet are closed automatically
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the email parameter in the query
            statement.setString(1, email);

            // Execute the query and process the result set
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Extract user details from the result set
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String motDePasse = rs.getString("motDePasse");
                    String role = rs.getString("role");
                    String tel = rs.getString("tel");
                    String imageUrl = rs.getString("imageUrl");
                    int codePostal = rs.getInt("codePostal");
                    String region = rs.getString("region");

                    // Create an Adresse object using the extracted address details
                    Adresse adresse = new Adresse(codePostal, region);

                    // Create and return a Utilisateur object
                    return new Utilisateur(id, nom, prenom, email, motDePasse, Role.valueOf(role), adresse, tel, imageUrl);
                } else {
                    System.out.println("User not found for email: " + email);
                    return null;
                }
            }
        } catch (SQLException e) {
            // Log the exception and rethrow it
            System.err.println("Error fetching user by email: " + e.getMessage());
            throw e; // Rethrow the exception to handle it at a higher level
        }
    }

    public Utilisateur getUserById(int id){
        // Define the SQL query to fetch user details along with address information
        String query = "SELECT u.id, u.nom, u.prenom, u.motDePasse, u.role, u.tel, u.email, u.imageUrl, " +
                "a.codePostal, a.region " +
                "FROM Utilisateur u JOIN Adresse a ON u.adresse_id = a.id WHERE u.id = ?";

        // Use try-with-resources to ensure the PreparedStatement and ResultSet are closed automatically
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the id parameter in the query
            statement.setInt(1, id);

            // Execute the query and process the result set
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Extract user details from the result set
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String email = rs.getString("email");
                    String motDePasse = rs.getString("motDePasse");
                    String role = rs.getString("role");
                    String tel = rs.getString("tel");
                    String imageUrl = rs.getString("imageUrl");
                    int codePostal = rs.getInt("codePostal");
                    String region = rs.getString("region");

                    // Create an Adresse object using the extracted address details
                    Adresse adresse = new Adresse(codePostal, region);

                    // Create and return a Utilisateur object
                    return new Utilisateur(id, nom, prenom, email, motDePasse, Role.valueOf(role), adresse, tel, imageUrl);
                } else {
                    System.out.println("User not found for id: " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by id: " + e.getMessage());
            return null;
        }

    }

    }



