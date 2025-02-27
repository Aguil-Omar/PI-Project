package com.pi_dev.controllers.GestionMateriel;

import com.pi_dev.models.GestionMateriels.Disponibilte;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.pi_dev.models.GestionMateriels.Materiels;  // Import de votre modèle Materiels
import com.pi_dev.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;// Import de votre classe DataSource
import com.pi_dev.models.GestionMateriels.Disponibilte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;


public class ReservationMateriels {

    @FXML
    private GridPane gridMateriels;  // Référence au GridPane défini dans le FXML

    private ObservableList<Materiels> listeMateriels = FXCollections.observableArrayList();  // Liste des matériels disponibles

    /**
     * Méthode appelée automatiquement après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Récupérer la liste des matériels disponibles depuis la base de données
        listeMateriels.setAll(getMaterielsDisponiblesDepuisBD());

        // Ajouter les cartes dans la grille
        afficherCartesMateriels();
    }

    /**
     * Affiche les cartes des matériels dans la grille.
     */
    private void afficherCartesMateriels() {
        gridMateriels.getChildren().clear();  // Vider la grille avant d'ajouter de nouveaux éléments
        int row = 0, col = 0;

        for (Materiels materiel : listeMateriels) {
            VBox carte = creerCarteMateriel(materiel);  // Créer une carte pour chaque matériel
            gridMateriels.add(carte, col, row);  // Ajouter la carte à la grille

            col++;
            if (col == 3) {  // Afficher 3 cartes par ligne
                col = 0;
                row++;
            }
        }
    }

    /**
     * Crée une carte visuelle pour un matériel.
     *
     * @param materiel Le matériel à afficher
     * @return Une VBox représentant la carte du matériel
     */
    private VBox creerCarteMateriel(Materiels materiel) {
        VBox carte = new VBox();
        carte.setStyle("-fx-border-color: #000; -fx-border-radius: 10px; -fx-padding: 10px; -fx-background-color: white;");
        carte.setPrefSize(200, 250);

        // Image du matériel (mettre une image par défaut si elle est absente)
        ImageView imageView = new ImageView(new Image("file:images/materiel.png")); // Changez avec une vraie URL d'image
        imageView.setFitWidth(180);
        imageView.setFitHeight(120);

        // Labels pour afficher les informations du matériel
        Label lblNom = new Label("Nom : " + materiel.getNom());
        Label lblPrix = new Label("Prix : " + materiel.getPrix() + "€");
        Label lblType = new Label("Type : " + materiel.getTypeMateriel().getNomM());
        //Label lblDescription = new Label("Description : " + materiel.getDescription());

        // Bouton pour réserver le matériel
        Button btnReserver = new Button("Réserver");
        btnReserver.setOnAction(e -> reserverMateriel(materiel));

        // Ajouter les éléments à la carte
        carte.getChildren().addAll(imageView, lblNom, lblPrix, lblType/*, lblDescription*/, btnReserver);
        return carte;
    }

    /**
     * Gère la réservation d'un matériel.
     *
     * @param materiel Le matériel à réserver
     */
    private void reserverMateriel(Materiels materiel) {
        int utilisateurId = 1;  // ID de l'utilisateur connecté (à adapter en fonction de ton système d'authentification)
        LocalDate dateDebut = LocalDate.now();  // Date de début (aujourd'hui)
        LocalDate dateFin = dateDebut.plusDays(7);  // Date de fin (par exemple, dans 7 jours)

        try (Connection connection = DataSource.getInstance().getConnection()) {
            if (connection != null && !connection.isClosed()) {
                // Insertion de la réservation dans la base de données
                String query = "INSERT INTO reservations (materiel_id, utilisateur_id, date_reservation, date_debut, date_fin, etat) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = connection.prepareStatement(query)) {
                    pst.setInt(1, materiel.getId());  // ID du matériel
                    pst.setInt(2, utilisateurId);     // ID de l'utilisateur
                    pst.setDate(3, Date.valueOf(LocalDate.now()));  // Date de réservation (aujourd'hui)
                    pst.setDate(4, Date.valueOf(dateDebut));  // Date de début
                    pst.setDate(5, Date.valueOf(dateFin));    // Date de fin
                    pst.setString(6, Disponibilte.INDISPONIBLE.name()); // L'état de la réservation est "en_attente" par défaut
                    pst.executeUpdate();

                    // Afficher une alerte ou message de succès
                    showAlert("Succès", "Réservation effectuée avec succès !");
                }
            } else {
                showAlert("Erreur", "La connexion à la base de données est fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la réservation.");
        }
    }


    /**
     * Affiche une alerte avec le titre et le message spécifié.
     */
    private void showAlert(String title, String message) {
        // Affiche un message d'alerte (vous pouvez adapter en fonction de votre framework/GUI)
        System.out.println(title + ": " + message);
    }

    /**
     * Récupère la liste des matériels disponibles depuis la base de données.
     *
     * @return Une ObservableList contenant les matériels disponibles
     */
    private ObservableList<Materiels> getMaterielsDisponiblesDepuisBD() {
        ObservableList<Materiels> materiels = FXCollections.observableArrayList();

        // Requête SQL pour récupérer les matériels disponibles (pas réservés)
        String query = "SELECT m.id, m.nom, m.prix, m.etat, m.type_materiel_id, m.image_path, tm.nomM, tm.description " +
                "FROM materiel m " +
                "JOIN type_materiel tm ON m.type_materiel_id = tm.id " +
                "WHERE m.id NOT IN (SELECT materiel_id FROM reservations WHERE etat = 'INDISPONIBLE')";

        // Utilisation de la DataSource pour obtenir une connexion
        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Récupérer les données de la base de données
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                float prix = resultSet.getFloat("prix");
                String description = resultSet.getString("description");
                String etatStr = resultSet.getString("etat");  // Récupérer l'état
                String imagePath = resultSet.getString("image_path");  // Récupérer le chemin de l'image
                String nomM = resultSet.getString("nomM");  // Récupérer le nom du type de matériel

                // Convertir String en enum Disponibilte
               Disponibilte etat;
                try {
                    etat = Disponibilte.valueOf(etatStr);  // Convertir en enum
                } catch (IllegalArgumentException e) {
                    etat = Disponibilte.DISPONIBLE;  // Valeur par défaut
                    System.err.println("Valeur d'état non reconnue : " + etatStr);
                }

                // Créer un objet TypeMateriels
                TypeMateriels typeMateriel = new TypeMateriels(nomM, description);

                // Créer un objet Materiels
                Materiels materiel = new Materiels(nom, prix, etat, typeMateriel, imagePath);

                // Ajouter le matériel à la liste
                materiels.add(materiel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération des matériels depuis la base de données.");
        }

        return materiels;
    }
}
