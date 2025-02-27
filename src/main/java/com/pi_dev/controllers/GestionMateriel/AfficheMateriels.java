package com.pi_dev.controllers.GestionMateriel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi_dev.models.GestionMateriels.Materiels;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.services.MaterielsServices;

import com.pi_dev.services.TypeMaterielsServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.PieChart;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class AfficheMateriels {

    @FXML
    private TableView<Materiels> tableMateriels;

    @FXML
    private TableColumn<Materiels, String> colNom;

    @FXML
    private TableColumn<Materiels, Float> colPrix;

    @FXML
    private TableColumn<Materiels, String> colEtat;

    @FXML
    private TableColumn<Materiels, String> colType;

    @FXML
    private TableColumn<Materiels, Void> colAction;
    @FXML
    private ComboBox<String> ctriM;
    @FXML
    private TextField tfrechercheM;
    private MaterielsServices MaterielsServices = new MaterielsServices();

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de Materiels
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeMateriel().getNomM()));
        ctriM.setItems(FXCollections.observableArrayList("A-Z", "Z-A", "Clear"));
        // Configuration de la colonne d'actions
        colAction.setCellFactory(createActionCellFactory());
        // Rafraîchir la liste des matériels
        rafraichirListe();
        // Ajuster la taille des colonnes dynamiquement
        colNom.prefWidthProperty().bind(tableMateriels.widthProperty().multiply(0.2)); // 20% de la largeur du tableau
        colPrix.prefWidthProperty().bind(tableMateriels.widthProperty().multiply(0.2));
        colEtat.prefWidthProperty().bind(tableMateriels.widthProperty().multiply(0.2));
        colType.prefWidthProperty().bind(tableMateriels.widthProperty().multiply(0.2));
        colAction.prefWidthProperty().bind(tableMateriels.widthProperty().multiply(0.2));
    }


    @FXML
    public void rafraichirListe() {
        List<Materiels> list = MaterielsServices.rechercher();
        System.out.println("Retrieved Data: " + list);  // Debugging

        ObservableList<Materiels> MaterielsObservableList = FXCollections.observableArrayList(list);
        tableMateriels.setItems(MaterielsObservableList);
        tableMateriels.refresh();  // Ensure UI update
    }
    private Callback<TableColumn<Materiels, Void>, TableCell<Materiels, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Action pour le bouton "Modifier"
                editButton.setOnAction(event -> {
                    Materiels materiel = getTableView().getItems().get(getIndex());
                    modifierMateriel(materiel);
                });

                // Action pour le bouton "Supprimer"
                deleteButton.setOnAction(event -> {
                    Materiels materiel = getTableView().getItems().get(getIndex());
                    supprimerMateriel(materiel);
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

    private void modifierMateriel(Materiels materiel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/ModifierMateriels.fxml"));
            Parent root = loader.load();

            ModifierMateriels controller = loader.getController();
            controller.setMateriel(materiel);

            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue de modification.");
        }
    }

    private void supprimerMateriel(Materiels materiel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/SupprimerMateriels.fxml"));
            Parent root = loader.load();

            SupprimerMateriel controller = loader.getController();
            controller.setMateriel(materiel);

            tableMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue de suppression.");
        }
    }

    @FXML
    private void Ajout() {
        naviguerVersAjout();
    }

    private void naviguerVersAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/AjoutMateriels.fxml"));
            Parent root = loader.load();
            Scene currentScene = tableMateriels.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue d'ajout.");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private MainInterface mainController;

    public void setMainController(MainInterface mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void switchToMaterielsTab() {
        if (mainController != null) {
            mainController.switchToMaterielsTab();
        }
    }
    public void tri(ActionEvent actionEvent) {
        String selectedOption = ctriM.getValue();
        if (selectedOption != null) {
            ObservableList<Materiels> MaterielsList = FXCollections.observableArrayList(MaterielsServices.rechercher());

            switch (selectedOption) {
                case "A-Z":
                    // Sort by 'type' in ascending order (A-Z)
                    FXCollections.sort(MaterielsList, Comparator.comparing(Materiels::getNom));
                    break;
                case "Z-A":
                    // Sort by 'type' in descending order (Z-A)
                    FXCollections.sort(MaterielsList, Comparator.comparing(Materiels::getNom).reversed());
                    break;
                case "Clear":
                    // Restore the original unsorted list
                    rafraichirListe();
                    return;  // Skip the sorting part if "Clear" is selected
            }

            // Update the TableView with the sorted list
            tableMateriels.setItems(MaterielsList);
        }
    }

    public void recherche(ActionEvent actionEvent) {
        String searchTerm = tfrechercheM.getText().toLowerCase();  // Get the search term and convert to lower case for case-insensitive search

        // Retrieve the list of TypeEspace
        List<Materiels> MaterielsList = MaterielsServices.rechercher();
        ObservableList<Materiels> filteredList = FXCollections.observableArrayList();

        // Filter the list based on the search term
        for (Materiels Materiels: MaterielsList) {
            if (Materiels.getNom().toLowerCase().contains(searchTerm)) {
                filteredList.add(Materiels);
            }
        }

        // Update the table with the filtered list
        tableMateriels.setItems(filteredList);
        tableMateriels.refresh();  // Refresh the table to reflect the search result
    }

    public void pdf(ActionEvent actionEvent) {
        try {
            // Step 1: Set up the PdfWriter and PdfDocument
            String dest = "C:\\\\Users\\\\MSI\\\\Desktop\\\\materiel\\\\GeneratedReport.pdf";  // Specify your file path
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);

            // Step 2: Create a Document
            Document document = new Document(pdf);

            // Step 3: Add content to the document
            document.add(new Paragraph("Les types des materiels"));

            // You can iterate over the items in your table and add them to the PDF
            for (Materiels Materiels :tableMateriels.getItems()) {
                document.add(new Paragraph("nom: " + Materiels.getNom()));
                document.add(new Paragraph("prix: " + Materiels.getPrix()));
                document.add(new Paragraph("etat: " + Materiels.getEtat()));
                document.add(new Paragraph("type: " + Materiels.getTypeMateriel()));

                document.add(new Paragraph("\n"));
            }

            // Step 4: Close the document and writer
            document.close();


        } catch (Exception e) {
            // Handle error

            e.printStackTrace();
        }
    }

    public void excel(ActionEvent actionEvent) {
        // File chooser to let the user select save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            return; // User canceled the save dialog
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(" Materiels");

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Nom", "Prix", "Etat", "TypeMateriel"};
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Populate Data
            int rowNum = 1;
            for (Materiels materiels : tableMateriels.getItems()) { // Assuming typeEspaceTable is your TableView
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(materiels.getNom());
                row.createCell(1).setCellValue(materiels.getPrix());
                row.createCell(2).setCellValue(materiels.getEtat().toString());
                row.createCell(3).setCellValue(materiels.getTypeMateriel().toString());
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            // Success message


        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    public void stat(ActionEvent actionEvent) {
        int availableCount = 0;
        int unavailableCount = 0;
        Map<String, Integer> typeMaterialCount = new HashMap<>();

        System.out.println("DEBUG: Checking Materiel -> TypeMateriel Mapping");
        var materielService = new MaterielsServices();
        var materials = materielService.rechercher();

        for (Materiels materiel : materials) {
            // Debugging: Check if TypeMateriel is NULL
            if (materiel.getTypeMateriel() == null) {
                System.out.println("Error: TypeMateriel is NULL for materiel ID: " + materiel.getId());
                continue; // Skip this entry if it has no type
            } else {
                System.out.println("Materiel ID: " + materiel.getId() + ", Type: " + materiel.getTypeMateriel().getNomM());
            }

            // Counting Availability
            if (materiel.getEtat().equals("Disponible")) {  // Assuming 'etat' is a string like "Disponible" or "Indisponible"
                availableCount++;
            } else {
                unavailableCount++;
            }

            // Counting Type Usage
            String type = materiel.getTypeMateriel().getNomM(); // Get the type of the material
            typeMaterialCount.put(type, typeMaterialCount.getOrDefault(type, 0) + 1);
        }

        int totalMateriels = availableCount + unavailableCount;

        // Pie Chart for Availability
        PieChart pieChartAvailability = new PieChart();
        PieChart.Data availableData = new PieChart.Data(
                "Disponible (" + calculatePercentage(availableCount, totalMateriels) + "%)", availableCount);
        PieChart.Data unavailableData = new PieChart.Data(
                "Indisponible (" + calculatePercentage(unavailableCount, totalMateriels) + "%)", unavailableCount);
        pieChartAvailability.getData().addAll(availableData, unavailableData);
        pieChartAvailability.setTitle("Disponibilité des Matériels");

        // Pie Chart for Type Usage
        PieChart pieChartTypeMateriel = new PieChart();
        for (Map.Entry<String, Integer> entry : typeMaterialCount.entrySet()) {
            int count = entry.getValue();
            pieChartTypeMateriel.getData().add(
                    new PieChart.Data(entry.getKey() + " (" + calculatePercentage(count, totalMateriels) + "%)", count));
        }
        pieChartTypeMateriel.setTitle("Utilisation des Types de Matériel");

        VBox vbox = new VBox(10, pieChartAvailability, pieChartTypeMateriel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Stage stage = new Stage();
        stage.setTitle("Statistiques des Matériels");
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }

    private int calculatePercentage(int part, int total) {
        if (total == 0) return 0;
        return (int) Math.round((part * 100.0) / total);
    }



}