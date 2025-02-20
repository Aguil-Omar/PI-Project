package com.pi_dev.controllers.GestionEspace;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.services.TypeEspaceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;



public class AfficheTypeEspace {

    @FXML
    private TableView<TypeEspace> typeTable;
    @FXML
    private TableColumn<TypeEspace, String> cType;
    @FXML
    private TableColumn<TypeEspace, String> cDescription;
    @FXML
    private TableColumn<TypeEspace, Void> cAction;
    @FXML
    private ComboBox<String> ctri;
    @FXML
    private TextField tfrecherche;

    private TypeEspaceService typeEspaceService = new TypeEspaceService();


    @FXML
    public void initialize() {
        ctri.setItems(FXCollections.observableArrayList("A-Z", "Z-A", "Clear"));
        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        cAction.setCellFactory(createActionCellFactory());
        rafraichirListe();
    }

    @FXML
    public void rafraichirListe() {
        List<TypeEspace> list = typeEspaceService.rechercher();
        System.out.println("Retrieved Data: " + list);  // Debugging

        ObservableList<TypeEspace> typeEspaceObservableList = FXCollections.observableArrayList(list);
        typeTable.setItems(typeEspaceObservableList);
        typeTable.refresh();  // Ensure UI update
    }

    private Callback<TableColumn<TypeEspace, Void>, TableCell<TypeEspace, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    TypeEspace typeEspace = getTableView().getItems().get(getIndex());
                    modifierTypeEspace(typeEspace);
                });
// Delete button action
                deleteButton.setOnAction(event -> {
                    TypeEspace typeEspace = getTableView().getItems().get(getIndex());
                    supprimerTypeEspace(typeEspace); // Delete action
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

    private void modifierTypeEspace(TypeEspace typeEspace) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceEspace/ModifierTypeEspace.fxml"));
            Parent root = loader.load();


            ModifierTypeEspace controller = loader.getController();
            controller.setTypeEspace(typeEspace);


            typeTable.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerTypeEspace(TypeEspace typeEspace) {
        try {
            typeEspaceService.supprimer(typeEspace); // Call to delete in the service
            rafraichirListe();  // Refresh the table after deletion
            showAlert("Success", "TypeEspace supprimé avec succès !", javafx.scene.control.Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Erreur lors de la suppression du TypeEspace.", javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Show alert method
    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
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

    public void ajoutt(ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/AjoutTypeEspace.fxml");
    }

    public void tri(ActionEvent actionEvent) {
        String selectedOption = ctri.getValue();
        if (selectedOption != null) {
            ObservableList<TypeEspace> typeEspaceList = FXCollections.observableArrayList(typeEspaceService.rechercher());

            switch (selectedOption) {
                case "A-Z":
                    // Sort by 'type' in ascending order (A-Z)
                    FXCollections.sort(typeEspaceList, Comparator.comparing(TypeEspace::getType));
                    break;
                case "Z-A":
                    // Sort by 'type' in descending order (Z-A)
                    FXCollections.sort(typeEspaceList, Comparator.comparing(TypeEspace::getType).reversed());
                    break;
                case "Clear":
                    // Restore the original unsorted list
                    rafraichirListe();
                    return;  // Skip the sorting part if "Clear" is selected
            }

            // Update the TableView with the sorted list
            typeTable.setItems(typeEspaceList);
        }
    }


    // Assuming you have this TextField in the FXML

    public void recherche(ActionEvent actionEvent) {
        String searchTerm = tfrecherche.getText().toLowerCase();  // Get the search term and convert to lower case for case-insensitive search

        // Retrieve the list of TypeEspace
        List<TypeEspace> typeEspaceList = typeEspaceService.rechercher();
        ObservableList<TypeEspace> filteredList = FXCollections.observableArrayList();

        // Filter the list based on the search term
        for (TypeEspace typeEspace : typeEspaceList) {
            if (typeEspace.getType().toLowerCase().contains(searchTerm)) {
                filteredList.add(typeEspace);
            }
        }

        // Update the table with the filtered list
        typeTable.setItems(filteredList);
        typeTable.refresh();  // Refresh the table to reflect the search result
    }


    public void pdf(ActionEvent actionEvent) {
        try {
            // Step 1: Set up the PdfWriter and PdfDocument
            String dest = "C:\\\\Users\\\\dell\\\\Desktop\\\\pdf\\\\type\\\\GeneratedReport.pdf";  // Specify your file path
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);

            // Step 2: Create a Document
            Document document = new Document(pdf);

            // Step 3: Add content to the document
            document.add(new Paragraph("Les types d'espace"));

            // You can iterate over the items in your table and add them to the PDF
            for (TypeEspace typeEspace : typeTable.getItems()) {
                document.add(new Paragraph("Type: " + typeEspace.getType()));
                document.add(new Paragraph("Description: " + typeEspace.getDescription()));
                document.add(new Paragraph("\n"));
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

