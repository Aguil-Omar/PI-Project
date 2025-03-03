package com.esprit.controllers.gestion_utilisateur;



import com.esprit.models.utilisateur.Utilisateur;
import com.esprit.services.utilisateur.AdminService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class adminDashControlleur {

    @FXML
    private ImageView adminIcon;

    @FXML
    private Label adminName;
    AdminService adminService=AdminService.getInstance();
    private Utilisateur user;
    @FXML
    public void initialize() {
        // Example: Load admin profile info
        adminName.setText("Super Admin");
        this.user=adminService.getLoggedUser();
        if (user != null) {
            adminName.setText(user.getNom() + " " + user.getPrenom());

            // Set the admin icon (image)
            String imageUrl = user.getImageUrl();  // Assuming imageUrl is a valid path or URL string

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Image image = new Image(imageUrl);  // Load image using the URL or path
                adminIcon.setImage(image);  // Set the image in the ImageView
            }
        } else {
            adminName.setText("Super Admin");  // Default fallback name if user is null
        }

    }

    @FXML
    void goToGestionUtilisateur() {
        System.out.println("Navigating to User Management...");
        // Load Gestion Utilisateur FXML here
    }

    @FXML
    void goToGestionEspace() {
        System.out.println("Navigating to Space Management...");
        // Load Gestion Espace FXML here
    }

    @FXML
    void goToGestionEvenement() {
        System.out.println("Navigating to Event Management...");
        // Load Gestion Événement FXML here
    }

    @FXML
    void goToGestionMateriels() {
        System.out.println("Navigating to Material Management...");
        // Load Gestion Matériels FXML here
    }

    @FXML
    void goToForum() {
        System.out.println("Navigating to Forum...");
        // Load Forum FXML here
    }
}
