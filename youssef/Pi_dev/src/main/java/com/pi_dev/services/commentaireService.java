
package com.pi_dev.services;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class commentaireService implements IService<commentaire> {
    private Connection connection = DataSource.getInstance().getConnection();

    public commentaireService() {
    }

    @Override
    public void ajouter(commentaire commentaire) {}

    public void ajouter(commentaire commentaire, evenement event,int idUser) {
        String req = "INSERT INTO commentaire(comment, date_comment, time_comment, nbr_likes, evenement_id,utilisateur_id) VALUES (?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setString(1, commentaire.getComment());
            ps.setDate(2, commentaire.getDate_comment());
            ps.setTime(3, commentaire.getTime_comment());
            ps.setInt(4, commentaire.getNbr_Likes());
            ps.setInt(5, event.getId());
            ps.setInt(6, idUser);
            ps.executeUpdate();
            System.out.println("comment added successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public void supprimer(commentaire commentaire) {
        String req = "DELETE from commentaire where id=?";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, commentaire.getId());
            ps.executeUpdate();
            System.out.println("comment deleted successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<commentaire> rechercher() {
        return List.of();
    }

    public List<commentaire> afficher() {
        List<commentaire> commentaireList = new ArrayList();
        String req = "SELECT * FROM commentaire";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                commentaireList.add(new commentaire(rs.getInt("id"), rs.getString("comment"), rs.getDate("date_comment"), rs.getTime("time_comment"), rs.getInt("nbr_likes"),rs.getInt("utilisateur_id"),rs.getInt("nbr_Dislikes")));
            }
        } catch (SQLException var5) {
            SQLException e = var5;
            System.out.println(e.getMessage());
        }

        return commentaireList;
    }

    public void modifier(commentaire c) {
        String req = "UPDATE commentaire set comment=?,date_comment=?,time_comment=?,nbr_likes=? where id=? ";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setString(1, c.getComment());
            ps.setDate(2, c.getDate_comment());
            ps.setTime(3, c.getTime_comment());
            ps.setInt(4, c.getNbr_Likes());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
            System.out.println("comment updated successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public List<commentaire> rechercher(evenement event) {
        List<commentaire> commentaireList = new ArrayList();
        String req = "SELECT * FROM commentaire WHERE evenement_id = ?";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, event.getId());
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                commentaireList.add(new commentaire(rs.getInt("id"), rs.getString("comment"), rs.getDate("date_comment"), rs.getTime("time_comment"), rs.getInt("nbr_likes"),rs.getInt("utilisateur_id"),rs.getInt("nbr_Dislikes")));
            }
        } catch (SQLException var5) {
            SQLException e = var5;
            System.out.println(e.getMessage());
        }

        return commentaireList;
    }

    public void addLike(commentaire c) {
        String req = "UPDATE commentaire set nbr_likes=nbr_likes+1 where id=?";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, c.getId());
            ps.executeUpdate();
            System.out.println("like increased successfully");
        } catch (SQLException var4) {
            SQLException e = var4;
            System.out.println(e.getMessage());
        }

    }

    public String getUserInteraction(int userId, int commentId) {
        String query = "SELECT interaction FROM comment_interaction WHERE utilisateur_id = ? AND commentaire_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, commentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("interaction");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleLikeDislike(int userId, int commentId, String interaction) {
        String checkQuery = "SELECT interaction FROM comment_interaction WHERE utilisateur_id = ? AND commentaire_id = ?";
        String insertQuery = "INSERT INTO comment_interaction (utilisateur_id, commentaire_id, interaction) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE comment_interaction SET interaction = ? WHERE utilisateur_id = ? AND commentaire_id = ?";
        String likeUpdateQuery = "UPDATE commentaire SET nbr_Likes = nbr_Likes + 1 WHERE id = ?";
        String dislikeUpdateQuery = "UPDATE commentaire SET nbr_Dislikes = nbr_Dislikes + 1 WHERE id = ?";
        String likeRevertQuery = "UPDATE commentaire SET nbr_Likes = nbr_Likes - 1 WHERE id = ?";
        String dislikeRevertQuery = "UPDATE commentaire SET nbr_Dislikes = nbr_Dislikes - 1 WHERE id = ?";

        try {
            // Check if the user has already interacted with the comment
            PreparedStatement checkPs = connection.prepareStatement(checkQuery);
            checkPs.setInt(1, userId);
            checkPs.setInt(2, commentId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                String existingInteraction = rs.getString("interaction");
                if (existingInteraction.equals(interaction)) {
                    // User is trying to undo their like/dislike
                    PreparedStatement revertPs;
                    if ("LIKE".equals(interaction)) {
                        revertPs = connection.prepareStatement(likeRevertQuery);
                    } else {
                        revertPs = connection.prepareStatement(dislikeRevertQuery);
                    }
                    revertPs.setInt(1, commentId);
                    revertPs.executeUpdate();

                    // Remove the interaction
                    PreparedStatement deletePs = connection.prepareStatement("DELETE FROM comment_interaction WHERE utilisateur_id = ? AND commentaire_id = ?");
                    deletePs.setInt(1, userId);
                    deletePs.setInt(2, commentId);
                    deletePs.executeUpdate();
                } else {
                    // User is changing their interaction (like to dislike or vice versa)
                    PreparedStatement updatePs = connection.prepareStatement(updateQuery);
                    updatePs.setString(1, interaction);
                    updatePs.setInt(2, userId);
                    updatePs.setInt(3, commentId);
                    updatePs.executeUpdate();

                    // Update the like/dislike count
                    PreparedStatement likeDislikePs;
                    if ("LIKE".equals(interaction)) {
                        likeDislikePs = connection.prepareStatement(likeUpdateQuery);
                    } else {
                        likeDislikePs = connection.prepareStatement(dislikeUpdateQuery);
                    }
                    likeDislikePs.setInt(1, commentId);
                    likeDislikePs.executeUpdate();
                }
            } else {
                // User is interacting for the first time
                PreparedStatement insertPs = connection.prepareStatement(insertQuery);
                insertPs.setInt(1, userId);
                insertPs.setInt(2, commentId);
                insertPs.setString(3, interaction);
                insertPs.executeUpdate();

                // Update the like/dislike count
                PreparedStatement likeDislikePs;
                if ("LIKE".equals(interaction)) {
                    likeDislikePs = connection.prepareStatement(likeUpdateQuery);
                } else {
                    likeDislikePs = connection.prepareStatement(dislikeUpdateQuery);
                }
                likeDislikePs.setInt(1, commentId);
                likeDislikePs.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public commentaire getCommentById(int commentId) {
        String query = "SELECT * FROM commentaire WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, commentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new commentaire(
                        rs.getInt("id"),
                        rs.getString("comment"),
                        rs.getDate("date_comment"),
                        rs.getTime("time_comment"),
                        rs.getInt("nbr_Likes"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("nbr_Dislikes")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
