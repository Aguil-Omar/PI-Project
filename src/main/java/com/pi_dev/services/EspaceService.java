package com.pi_dev.services;


import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.utils.DataSource;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Espace espace) {
        try {
            // Upload the image to the specified location under www
            if (espace.getimageUrl() != null) {
                String sourceFilePath = espace.getimageUrl(); // Source file path
                String destinationDir = "C:\\wamp64\\www\\images"; // Destination directory under www
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
                String imageUrl = "images/" + sourceFile.getName(); // Adjust relative URL
                espace.setimageUrl(imageUrl);
            }

            String req = "INSERT INTO espace (nom, localisation, etat, type_espace_id, imageUrl) VALUES (?,?,?,?,?)";
            try (PreparedStatement pst = connection.prepareStatement(req)) { // Auto-closeable
                pst.setString(1, espace.getNom());
                pst.setString(2, espace.getLocalisation());
                pst.setString(3, espace.getEtat().toString());

                // Avoid NullPointerException
                if (espace.getTypeEspace() != null) {
                    pst.setInt(4, espace.getTypeEspace().getId());
                } else {
                    pst.setNull(4, Types.INTEGER);
                }

                pst.setString(5, espace.getimageUrl());
                pst.executeUpdate();
                System.out.println("Espace ajouté");
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
    public void modifier(Espace espace) {
        String req = "UPDATE espace SET nom=?, localisation=?, etat=?, type_espace_id=?, imageUrl=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getLocalisation());
            pst.setString(3, espace.getEtat().toString());  // Enum to String
            pst.setInt(4, espace.getTypeEspace().getId());
            pst.setString(5, espace.getimageUrl());
            pst.setInt(6, espace.getId());
            pst.executeUpdate();
            System.out.println("Espace modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Espace espace) {
        // Delete type_espace record by id
        String req = "DELETE FROM espace WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, espace.getId());
            pst.executeUpdate();
            System.out.println("Espace supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Espace> rechercher() {
        List<Espace> espaces = new ArrayList<>();

        String req = "SELECT * FROM espace";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Convert String to Enum for etat
                Disponibilite etat = Disponibilite.valueOf(rs.getString("etat"));
                // Create TypeEspace object with the foreign key
                TypeEspace typeEspace = new TypeEspace(rs.getInt("type_espace_id"));

                // Create Espace object and add it to the list
                Espace espace = new Espace(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("localisation"),
                        etat,  // Set Disponibilite enum
                        typeEspace
                        , rs.getString("imageUrl")

                );
                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des espaces : " + e.getMessage());
        }


        return espaces;  // Return the list of Espace objects
    }
    public List<Espace> fetchAllEspaces() {
        List<Espace> espaces = new ArrayList<>();
        String query = "SELECT e.id, e.nom, e.etat, te.id AS type_id, te.type " +
                "FROM espace e " +
                "JOIN type_espace te ON e.type_espace_id = te.id";

        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Disponibilite etat = Disponibilite.valueOf(rs.getString("etat"));
                int typeId = rs.getInt("type_id");
                String typeName = rs.getString("type");

                // Correctly create TypeEspace object with id and type
                TypeEspace typeEspace = new TypeEspace(typeId, typeName);

                // Create Espace object with linked TypeEspace
                Espace espace = new Espace(id, nom, etat, typeEspace);
                espaces.add(espace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return espaces;
    }





}
