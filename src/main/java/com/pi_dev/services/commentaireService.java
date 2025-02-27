package com.pi_dev.services;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commentaireService implements IService<commentaire> {
    private final Connection connection;

    public commentaireService() {
        try {
            this.connection = DataSource.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Erreur de connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void ajouter(commentaire commentaire) {
        // Cette méthode est vide car il existe une version qui prend en charge un événement spécifique.
    }

    // Ajouter un commentaire associé à un événement spécifique
    public void ajouter(commentaire commentaire, evenement event) {
        String req = "INSERT INTO commentaire(comment, date_comment, time_comment, nbr_likes, evenement_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = this.connection.prepareStatement(req)) {
            ps.setString(1, commentaire.getComment());
            ps.setDate(2, commentaire.getDate_comment());
            ps.setTime(3, commentaire.getTime_comment());
            ps.setInt(4, commentaire.getNbr_Likes());
            ps.setInt(5, event.getId());
            ps.executeUpdate();
            System.out.println("✅ Commentaire ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du commentaire : " + e.getMessage());
        }
    }

    // Supprimer un commentaire
    public void supprimer(commentaire commentaire) {
        String req = "DELETE from commentaire WHERE id=?";

        try (PreparedStatement ps = this.connection.prepareStatement(req)) {
            ps.setInt(1, commentaire.getId());
            ps.executeUpdate();
            System.out.println("✅ Commentaire supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du commentaire : " + e.getMessage());
        }
    }

    @Override
    public List<commentaire> rechercher() {
        return new ArrayList<>();  // Retourne une liste vide
    }

    // Modifier un commentaire
    public void modifier(commentaire c) {
        String req = "UPDATE commentaire SET comment=?, date_comment=?, time_comment=?, nbr_likes=? WHERE id=?";

        try (PreparedStatement ps = this.connection.prepareStatement(req)) {
            ps.setString(1, c.getComment());
            ps.setDate(2, c.getDate_comment());
            ps.setTime(3, c.getTime_comment());
            ps.setInt(4, c.getNbr_Likes());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
            System.out.println("✅ Commentaire mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour du commentaire : " + e.getMessage());
        }
    }

    // Rechercher les commentaires d'un événement spécifique
    public List<commentaire> rechercher(evenement event) {
        List<commentaire> commentaireList = new ArrayList<>();
        String req = "SELECT * FROM commentaire WHERE evenement_id = ?";

        try (PreparedStatement ps = this.connection.prepareStatement(req)) {
            ps.setInt(1, event.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    commentaireList.add(new commentaire(
                            rs.getInt("id"),
                            rs.getString("comment"),
                            rs.getDate("date_comment"),
                            rs.getTime("time_comment"),
                            rs.getInt("nbr_likes")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des commentaires : " + e.getMessage());
        }

        return commentaireList;
    }

    // Ajouter un like à un commentaire
    public void addLike(commentaire c) {
        String req = "UPDATE commentaire SET nbr_likes = nbr_likes + 1 WHERE id=?";

        try (PreparedStatement ps = this.connection.prepareStatement(req)) {
            ps.setInt(1, c.getId());
            ps.executeUpdate();
            System.out.println("✅ Like ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du like : " + e.getMessage());
        }
    }
}
