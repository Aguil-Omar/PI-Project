package com.pi_dev.controllers.GestionEspace;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.services.EspaceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AfficheEspace implements Initializable {

    @FXML
    private TableView<Espace> espaceTable;
    @FXML
    private Button ajout;

    @FXML
    private TableColumn<Espace, String> cNom;

    @FXML
    private TableColumn<Espace, String> cLocalisation;


    @FXML
    private TableColumn<Espace, String> cDisponible;
    @FXML
    private TableColumn<Espace, Void> caction;
    @FXML
    private Button tri;
    @FXML
    private Button recherche;
    @FXML
    private ComboBox<String> ctri;
@FXML
private TextField tfrecherche;

    private ObservableList<Espace> espaces = FXCollections.observableArrayList();
    private EspaceService espaceService = new EspaceService();  // ✅ Create an instance of EspaceService

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ctri.setItems(FXCollections.observableArrayList("A-Z", "Z-A", "Disponible", "Indisponible", "Clear"));
        // Displaying the name of the space
        cNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        // Displaying the localisation of the space
        cLocalisation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));

        // Displaying the availability (Etat) using the enum's string representation
        cDisponible.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        caction.setCellFactory(createActionCellFactory());

        refreshTableView(); // ✅ Load data from DB when the scene initializes
    }



    public void refreshTableView() {
        espaces = FXCollections.observableArrayList(espaceService.rechercher()); // Store data in 'espaces'
        espaceTable.setItems(espaces);
    }
    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    private Callback<TableColumn<Espace, Void>, TableCell<Espace, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style buttons (optional)
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Espace espace = getTableView().getItems().get(getIndex());
                    modifierEspace(espace);
                });

                deleteButton.setOnAction(event -> {
                    Espace espace = getTableView().getItems().get(getIndex());
                    supprimerEspace(espace);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        };
    }
    private void modifierEspace(Espace espace) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEspace/ModifierEspace.fxml"));
            Parent root = loader.load();

            ModifierEspace controller = loader.getController();
            controller.setEspace(espace);

            espaceTable.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerEspace(Espace espace) {
        try {
            espaceService.supprimer(espace);  // Call to delete in the service
            refreshTableView();  // Refresh the table after deletion

            // Show success alert
            showAlert("Success", "Espace supprimé avec succès !", javafx.scene.control.Alert.AlertType.INFORMATION);

            // Log the deletion to the console
            System.out.println("Espace supprimé: " + espace.getNom());
        } catch (Exception e) {
            // Show error alert in case of failure
            showAlert("Error", "Erreur lors de la suppression de l'Espace.", javafx.scene.control.Alert.AlertType.ERROR);

            // Log the error to the console
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void ajout(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/AjoutEspace.fxml");
    }
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retour(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/Homepage.fxml");
    }

    public void tri(ActionEvent actionEvent) {
        String selectedOption = ctri.getValue();
        if (selectedOption != null) {
            ObservableList<Espace> espaceList = FXCollections.observableArrayList(espaces); // Get full list

            switch (selectedOption) {
                case "A-Z":
                    FXCollections.sort(espaceList, Comparator.comparing(Espace::getNom));
                    break;
                case "Z-A":
                    FXCollections.sort(espaceList, Comparator.comparing(Espace::getNom).reversed());
                    break;
                case "Disponible":
                    espaceList = espaceList.filtered(espace -> espace.getEtat() == Disponibilite.DISPONIBLE);
                    break;
                case "Indisponible":
                    espaceList = espaceList.filtered(espace -> espace.getEtat() == Disponibilite.INDISPONIBLE);
                    break;
                case "Clear":
                    // Clear the sorting and filtering, restore all elements
                    refreshTableView();
                    return; // Skip further processing if "Clear" is selected
            }

            espaceTable.setItems(espaceList); // Update the table
        }
    }






    public void recherche(ActionEvent actionEvent) {
        String searchText = tfrecherche.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Espace> filteredList = FXCollections.observableArrayList();
            for (Espace espace : espaces) { // Loop through the full list
                if (espace.getNom().toLowerCase().contains(searchText)) {
                    filteredList.add(espace);
                }
            }
            espaceTable.setItems(filteredList); // Update table with results
        } else {
            espaceTable.setItems(espaces); // Restore full list when search is cleared
        }
    }


    public void pdf(ActionEvent actionEvent) {
        try {
            // Step 1: Set up the PdfWriter and PdfDocument
            String dest = "C:\\Users\\dell\\Desktop\\pdf\\espace\\GeneratedEspaceReport.pdf";  // Specify your file path
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);

            // Step 2: Create a Document
            Document document = new Document(pdf);

            // Step 3: Add content to the document
            document.add(new Paragraph("Liste des espaces"));

            // Iterate over the items in your table and add them to the PDF
            for (Espace espace : espaceTable.getItems()) {
                document.add(new Paragraph("Nom de l'espace: " + espace.getNom()));  // Nom of the Espace
                document.add(new Paragraph("Localisation: " + espace.getLocalisation()));  // Localisation of the Espace
                document.add(new Paragraph("État: " + espace.getEtat().toString()));  // Etat (enum) of the Espace
                document.add(new Paragraph("\n"));  // Add a blank line after each entry
            }

            // Step 4: Close the document and writer
            document.close();

            // Show success alert
            showAlert("Success", "PDF generated successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            // Handle error
            showAlert("Error", "An error occurred while generating the PDF.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
