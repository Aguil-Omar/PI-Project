
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

    public void ajouter(commentaire commentaire, evenement event) {
        String req = "INSERT INTO commentaire(comment, date_comment, time_comment, nbr_likes, evenement_id) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setString(1, commentaire.getComment());
            ps.setDate(2, commentaire.getDate_comment());
            ps.setTime(3, commentaire.getTime_comment());
            ps.setInt(4, commentaire.getNbr_Likes());
            ps.setInt(5, event.getId());
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
                commentaireList.add(new commentaire(rs.getInt("id"), rs.getString("comment"), rs.getDate("date_comment"), rs.getTime("time_comment"), rs.getInt("nbr_likes")));
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
}
