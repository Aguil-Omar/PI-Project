package com.esprit.services;

import com.esprit.models.Adresse;
import com.esprit.models.Role;
import com.esprit.models.Utilisateur;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminService implements IServices<Utilisateur> {

    private Connection connection = DataSource.getInstance().getConnection();
    @Override
    public void ajouter(Utilisateur utilisateur) {
        try {
            String query = "INSERT INTO Adresse (codePostal, region) VALUES (?, ?)";
            PreparedStatement adresseStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            adresseStmt.setInt(1, utilisateur.getAdresse().getCodePostal());
            adresseStmt.setString(2, utilisateur.getAdresse().getRegion());
            adresseStmt.executeUpdate();

            int adresseId = 0;
            ResultSet rs = adresseStmt.getGeneratedKeys();
            if (rs.next()) {
                adresseId = rs.getInt(1);
            }

            String userQuery = "INSERT INTO Utilisateur (nom, prenom, motDePasse, role, adresse_id, tel) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement userStmt = connection.prepareStatement(userQuery);
            userStmt.setString(1, utilisateur.getNom());
            userStmt.setString(2, utilisateur.getPrenom());
            userStmt.setString(3, utilisateur.getMotDePasse());
            userStmt.setString(4, utilisateur.getRole().toString());
            userStmt.setInt(5, adresseId);
            userStmt.setString(6, utilisateur.getTel());
            userStmt.executeUpdate();

            System.out.println("Utilisateur ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Utilisateur> rechercher() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            String query = "SELECT u.id, u.nom, u.prenom, u.motDePasse, u.role, u.tel, " +
                    "a.codePostal, a.region " +
                    "FROM Utilisateur u JOIN Adresse a ON u.adresse_id = a.id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String motDePasse = rs.getString("motDePasse");
                String role = rs.getString("role");
                String tel = rs.getString("tel");
                int codePostal = rs.getInt("codePostal");
                String region = rs.getString("region");

                Adresse adresse = new Adresse(codePostal, region);
                Utilisateur utilisateur = new Utilisateur(id, nom, prenom, motDePasse, Role.valueOf(role), adresse, tel);

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
            // Update Adresse
            String adresseQuery = "UPDATE Adresse SET codePostal = ?, region = ? WHERE id = (SELECT adresse_id FROM Utilisateur WHERE id = ?)";
            PreparedStatement adresseStmt = connection.prepareStatement(adresseQuery);
            adresseStmt.setInt(1, utilisateur.getAdresse().getCodePostal());
            adresseStmt.setString(2, utilisateur.getAdresse().getRegion());
            adresseStmt.setInt(3, utilisateur.getId());
            adresseStmt.executeUpdate();

            // Update Utilisateur
            String utilisateurQuery = "UPDATE Utilisateur SET nom = ?, prenom = ?, motDePasse = ?, role = ?, tel = ? WHERE id = ?";
            PreparedStatement utilisateurStmt = connection.prepareStatement(utilisateurQuery);
            utilisateurStmt.setString(1, utilisateur.getNom());
            utilisateurStmt.setString(2, utilisateur.getPrenom());
            utilisateurStmt.setString(3, utilisateur.getMotDePasse());
            utilisateurStmt.setString(4, utilisateur.getRole().toString());
            utilisateurStmt.setString(5, utilisateur.getTel());
            utilisateurStmt.setInt(6, utilisateur.getId());
            utilisateurStmt.executeUpdate();

            System.out.println("Utilisateur modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
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
