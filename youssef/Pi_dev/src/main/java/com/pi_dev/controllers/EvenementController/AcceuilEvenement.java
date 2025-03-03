package com.pi_dev.controllers.EvenementController;

import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.EvenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class AcceuilEvenement  {

    @FXML
    private ListView<VBox> listViewEvenements;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;


    private EvenService eventService = new EvenService();
    private ObservableList<VBox> originalItems; // Liste originale des événements
    private FilteredList<VBox> filteredItems;
    private Utilisateur loggedUser;// Liste filtrée

    public void initialize() {
        AdminService adminService = AdminService.getInstance();
        this.loggedUser = adminService.getLoggedUser(); // Fetch logged user from singleton
        System.out.println(loggedUser);

        // Charger les événements
        chargerEvenements();

        // Configurer la recherche
        configurerRecherche();

        // Configurer le filtre par catégorie
        configurerFiltreCategorie();


        // Ajouter un écouteur d'événements pour détecter les clics sur les cellules
        listViewEvenements.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Vérifier si c'est un simple clic
                VBox selectedItem = listViewEvenements.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Récupérer le contrôleur associé à l'élément sélectionné
                    EvenementItem controller = (EvenementItem) selectedItem.getProperties().get("controller");
                    if (controller != null) {
                        // Simuler un clic sur le bouton "More Details"
                        controller.handleMoreDetails(null);
                    }
                }
            }

        });
    }


    private void chargerEvenements() {
        List<evenement> evenements = eventService.rechercher();

        // Convertir les événements en VBox pour la ListView
        originalItems = FXCollections.observableArrayList();
        for (evenement evt : evenements) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEvenement/EventItem.fxml"));
                VBox itemPane = loader.load();

                EvenementItem controller = loader.getController();
                controller.setEvenement(evt);
                itemPane.getProperties().put("controller", controller);

                originalItems.add(itemPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Appliquer la liste originale à la ListView
        listViewEvenements.setItems(originalItems);
    }

    private void configurerRecherche() {
        // Créer une FilteredList pour filtrer les éléments
        filteredItems = new FilteredList<>(originalItems, p -> true);

        // Lier le champ de recherche à la FilteredList
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                // Si le champ de recherche est vide, afficher tous les éléments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Récupérer le contrôleur associé à l'élément
                EvenementItem controller = (EvenementItem) item.getProperties().get("controller");
                if (controller != null) {
                    // Comparer le nom de l'événement avec le texte saisi
                    String nomEvenement = controller.getEventTitle().getText().toLowerCase();
                    return nomEvenement.contains(newValue.toLowerCase());
                }
                return false;
            });
        });

        // Créer une SortedList pour trier les éléments filtrés
        SortedList<VBox> sortedItems = new SortedList<>(filteredItems);

        // Lier la SortedList à la ListView
        listViewEvenements.setItems(sortedItems);
    }

    private void configurerFiltreCategorie() {
        // Ajouter les catégories disponibles au ComboBox
        categoryFilter.getItems().addAll("Seminaire","Conference","Atelier");

        // Définir la valeur par défaut
        categoryFilter.setValue("Tous");

        // Ajouter un écouteur pour filtrer les événements en fonction de la catégorie sélectionnée
        categoryFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.equals("Tous")) {
                    return true;
                }

                // Récupérer le contrôleur associé à l'élément
                EvenementItem controller = (EvenementItem) item.getProperties().get("controller");
                if (controller != null) {
                    // Utiliser la méthode getEvenement() pour accéder à la catégorie
                    evenement evt = controller.getEvenement();
                    if (evt != null) {
                        // Convertir la catégorie en String si nécessaire
                        String categorieEvenement = evt.getCategorie().toString();
                        return categorieEvenement.equals(newValue);
                    }
                }
                return false;
            });
        });
    }

    public void openchatbot(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML du chatbot
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceComment/chatBot.fxml")); // Ajustez le chemin
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage)
            Stage chatStage = new Stage();
            chatStage.initModality(Modality.APPLICATION_MODAL); // Empêche l'interaction avec la fenêtre principale
            chatStage.initStyle(StageStyle.UTILITY); // Style de fenêtre simple
            chatStage.setTitle("ChatBot");
            chatStage.setScene(scene);
            chatStage.setResizable(false); // Empêcher le redimensionnement
            chatStage.show(); // Afficher la fenêtre
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStatisticFeedBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceComment/statistics.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage)
            Stage chatStage = new Stage();
            chatStage.initModality(Modality.APPLICATION_MODAL); // Empêche l'interaction avec la fenêtre principale
            chatStage.initStyle(StageStyle.UTILITY); // Style de fenêtre simple
            chatStage.setTitle("feed back statistics");
            chatStage.setScene(scene);
            chatStage.setResizable(false); // Empêcher le redimensionnement
            chatStage.show(); // Afficher la fenêtre
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}