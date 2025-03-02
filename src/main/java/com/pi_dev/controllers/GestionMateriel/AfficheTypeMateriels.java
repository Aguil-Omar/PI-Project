package com.pi_dev.controllers.GestionMateriel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.models.GestionMateriels.TypeMateriels;
import com.pi_dev.services.TypeMaterielsServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.scene.Scene;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class AfficheTypeMateriels {

    @FXML
    private TableView<TypeMateriels> tableTypeMateriels;

    @FXML
    private TableColumn<TypeMateriels, String> colNomM;

    @FXML
    private TableColumn<TypeMateriels, String> colDescription;

    @FXML
    private TableColumn<TypeMateriels, Void> colAction;
    @FXML
    private ComboBox<String> ctri;
    @FXML
    private TextField tfrecherche;
    private TypeMaterielsServices typeMaterielsServices = new TypeMaterielsServices();

    @FXML
    public void initialize() {

        colNomM.setCellValueFactory(new PropertyValueFactory<>("nomM"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAction.setCellFactory(createActionCellFactory());
        ctri.setItems(FXCollections.observableArrayList("A-Z", "Z-A", "Clear"));
        colNomM.prefWidthProperty().bind(tableTypeMateriels.widthProperty().multiply(0.3)); // 30%
        colDescription.prefWidthProperty().bind(tableTypeMateriels.widthProperty().multiply(0.5)); // 50%
        colAction.prefWidthProperty().bind(tableTypeMateriels.widthProperty().multiply(0.2)); // 20% (plus petit)


        rafraichirListe();
    }

    @FXML
    public void rafraichirListe() {
        List<TypeMateriels> list = typeMaterielsServices.rechercher();
        System.out.println("Retrieved Data: " + list);  // Debugging

        ObservableList<TypeMateriels> typeMaterielsObservableList = FXCollections.observableArrayList(list);
        tableTypeMateriels.setItems(typeMaterielsObservableList);
        tableTypeMateriels.refresh();  // Ensure UI update
    }

    private Callback<TableColumn<TypeMateriels, Void>, TableCell<TypeMateriels, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {

                editButton.setOnAction(event -> {
                    TypeMateriels typeMateriel = getTableView().getItems().get(getIndex());
                    modifierTypeMateriel(typeMateriel);
                });


                deleteButton.setOnAction(event -> {
                    TypeMateriels typeMateriel = getTableView().getItems().get(getIndex());
                    supprimerTypeMateriel(typeMateriel);
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


    private void modifierTypeMateriel(TypeMateriels typeMateriel) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/ModifierTypeMateriels.fxml"));
            Parent root = loader.load();


            ModifierTypeMateriels controller = loader.getController();
            controller.setTypeMateriel(typeMateriel);


            tableTypeMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void supprimerTypeMateriel(TypeMateriels typeMateriel) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/SupprimerTypeMateriels.fxml"));
            Parent root = loader.load();


            SupprimerTypeMateriels controller = loader.getController();
            controller.setTypeMateriel(typeMateriel);


            tableTypeMateriels.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Ajout() {
        naviguerVersAjout();
    }

    private void naviguerVersAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMateriel/AjoutTypeMateriels.fxml"));
            Parent root = loader.load();
            Scene currentScene = tableTypeMateriels.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MainInterface MainController;

    public void setMainController(MainInterface mainController) {
        this.MainController = mainController;
    }


    @FXML
    private void switchToMaterielsTab() {
        if (MainController != null) {
            MainController.switchToMaterielsTab();
        }
    }

    public void tri(ActionEvent actionEvent) {
        String selectedOption = ctri.getValue();
        if (selectedOption != null) {
            ObservableList<TypeMateriels> typeMaterielsList = FXCollections.observableArrayList(typeMaterielsServices.rechercher());

            switch (selectedOption) {
                case "A-Z":
                    // Sort by 'type' in ascending order (A-Z)
                    FXCollections.sort(typeMaterielsList, Comparator.comparing(TypeMateriels::getNomM));
                    break;
                case "Z-A":
                    // Sort by 'type' in descending order (Z-A)
                    FXCollections.sort(typeMaterielsList, Comparator.comparing(TypeMateriels::getNomM).reversed());
                    break;
                case "Clear":
                    // Restore the original unsorted list
                    rafraichirListe();
                    return;  // Skip the sorting part if "Clear" is selected
            }

            // Update the TableView with the sorted list
            tableTypeMateriels.setItems(typeMaterielsList);
        }
    }

    public void recherche(ActionEvent actionEvent) {
        String searchTerm = tfrecherche.getText().toLowerCase();  // Get the search term and convert to lower case for case-insensitive search

        // Retrieve the list of TypeEspace
        List<TypeMateriels> typeEspaceList = typeMaterielsServices.rechercher();
        ObservableList<TypeMateriels> filteredList = FXCollections.observableArrayList();

        // Filter the list based on the search term
        for (TypeMateriels typeMateriels: typeEspaceList) {
            if (typeMateriels.getNomM().toLowerCase().contains(searchTerm)) {
                filteredList.add(typeMateriels);
            }
        }

        // Update the table with the filtered list
        tableTypeMateriels.setItems(filteredList);
        tableTypeMateriels.refresh();  // Refresh the table to reflect the search result
    }

    public void pdf(ActionEvent actionEvent) {
        try {//C:\Users\MSI\Desktop\kharya
            // Step 1: Set up the PdfWriter and PdfDocument
            String dest = "C:\\\\Users\\\\MSI\\\\Desktop\\\\kharya\\\\GeneratedReport.pdf";  // Specify your file path
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);

            // Step 2: Create a Document
            Document document = new Document(pdf);

            // Step 3: Add content to the document
            document.add(new Paragraph("Les types des materiels"));

            // You can iterate over the items in your table and add them to the PDF
            for (TypeMateriels typeMateriels :tableTypeMateriels.getItems()) {
                document.add(new Paragraph("Type: " + typeMateriels.getNomM()));
                document.add(new Paragraph("Description: " + typeMateriels.getDescription()));
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
            Sheet sheet = workbook.createSheet("Types Materiels");

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Nom", "Description"};
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Populate Data
            int rowNum = 1;
            for (TypeMateriels typeMateriels : tableTypeMateriels.getItems()) { // Assuming typeEspaceTable is your TableView
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(typeMateriels.getNomM());
                row.createCell(1).setCellValue(typeMateriels.getDescription());
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

}