package com.pi_dev.services;
import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class EvenService implements IService<evenement> {
    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public void ajouter(evenement evenement) {
        String req = "INSERT INTO evenement (titre, description, date, statut, categorie) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setDate(3, new java.sql.Date(evenement.getDate().getTime()));
            pst.setString(4, evenement.getStatut());
            pst.setString(5, evenement.getCategorie().name());
            pst.executeUpdate();
            System.out.println("Événement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifier(evenement evenement) {
        String req = "UPDATE evenement SET titre = ?, description = ?, date = ?, statut = ?, categorie = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setDate(3, new java.sql.Date(evenement.getDate().getTime()));
            pst.setString(4, evenement.getStatut());
            pst.setString(5, evenement.getCategorie().name());
            pst.setInt(6, evenement.getId()); // Vous devez passer l'ID
            pst.executeUpdate();
            System.out.println("Événement modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }
    @Override
    public void supprimer(evenement evenement) {
        String req = "DELETE FROM evenement WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, evenement.getId());
            pst.executeUpdate();
            System.out.println("Événement supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
    @Override
    public List<evenement> rechercher() {
        List<evenement> evenements = new ArrayList<>();

        String req = "SELECT * FROM evenement";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                evenement evt = new evenement(
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        rs.getString("statut"),
                        Categorie.valueOf(rs.getString("categorie"))
                );
                evenements.add(evt);  // Ajouter l'événement à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }

        return evenements;  // Retourner la liste des événements
    }





}
