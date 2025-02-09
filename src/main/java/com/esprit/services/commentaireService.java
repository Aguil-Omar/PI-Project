
package com.esprit.services;

import com.esprit.models.commentaire;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class commentaireService implements Iservice<commentaire> {
    private Connection connection = DataSource.getInstance().getConnection();

    public commentaireService() {
    }

    public void ajouter(commentaire commentaire) {
        String req = "INSERT into commentaire(comment,date_comment,time_comment,nbr_likes) values(?,?,?,?)";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setString(1, commentaire.getComment());
            ps.setDate(2, commentaire.getDate_comment());
            ps.setTime(3, commentaire.getTime_comment());
            ps.setInt(4, commentaire.getNbr_Likes());
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

    public List<commentaire> afficher() {
        List<commentaire> commentaireList = new ArrayList();
        String req = "SELECT * FROM commentaire";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
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
