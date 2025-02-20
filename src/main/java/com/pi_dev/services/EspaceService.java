package com.pi_dev.services;


import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.utils.DataSource;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspaceService implements IService<Espace> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Espace espace) {
        String req = "INSERT INTO espace (nom, localisation, etat, type_espace_id) VALUES (?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getLocalisation());
            pst.setString(3, espace.getEtat().toString());
            pst.setInt(4, espace.getTypeEspace().getId());  // Assuming TypeEspace has getId()
            pst.executeUpdate();
            System.out.println("Espace ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Espace espace) {
        String req = "UPDATE espace SET nom=?, localisation=?, etat=?, type_espace_id=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, espace.getNom());
            pst.setString(2, espace.getLocalisation());
            pst.setString(3, espace.getEtat().toString());  // Enum to String
            pst.setInt(4, espace.getTypeEspace().getId());  // Assuming TypeEspace has getId()
            pst.setInt(5, espace.getId());
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
                        typeEspace  // Set TypeEspace object
                );
                espaces.add(espace);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des espaces : " + e.getMessage());
        }

        return espaces;  // Return the list of Espace objects
    }

}
