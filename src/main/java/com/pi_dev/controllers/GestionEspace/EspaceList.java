package com.pi_dev.controllers.GestionEspace;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.pi_dev.services.TypeEspaceService;
import javafx.scene.paint.Color;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.utils.DataSource;
import javafx.geometry.Insets;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.apache.poi.ss.usermodel.Font;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.services.EspaceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;

public class EspaceList implements Initializable {
    @FXML
    private TableView<Espace> espaceTable;
    @FXML
    private Button ajout;
    @FXML
    private TableColumn<Espace, String> cNom;
    @FXML
    private ImageView qrI;
    @FXML
    private TableColumn<Espace, String> cLocalisation;
    @FXML
    private TableColumn<Espace, String> cimage;
    @FXML
    private TableColumn<Espace, String> cDisponible;
    @FXML
    private TableColumn<Espace, Void> caction;
    @FXML
    private TableColumn<Espace, String> ctype;
    @FXML
    private TableColumn<Espace, String> cdescription;
    @FXML
    private Button tri;
    @FXML
    private Button recherche;
    @FXML
    private ComboBox<String> ctri;
    @FXML
    private TextField tfrecherche;
    private EspaceService espaceService = new EspaceService();
    private ObservableList<Espace> espaces = FXCollections.observableArrayList();
    private TypeEspaceService typeEspaceService = new TypeEspaceService();
    private int evenementId;

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ctri.setItems(FXCollections.observableArrayList("A-Z", "Z-A", "Disponible", "Indisponible", "Clear"));
        cNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        cLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        cDisponible.setCellValueFactory(new PropertyValueFactory<>("etat"));
        ctype.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeEspace().getType()));
        cdescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeEspace().getDescription()));
        tfrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the search method every time the user types in the TextField
            searchEspace(newValue);
        });
        cimage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getimageUrl()));
        cimage.setCellFactory(new Callback<TableColumn<Espace, String>, TableCell<Espace, String>>() {
            @Override
            public TableCell<Espace, String> call(TableColumn<Espace, String> param) {
                return new TableCell<Espace, String>() {
                    private final ImageView imageView = new ImageView();
                    @Override
                    protected void updateItem(String imageUrl, boolean empty) {
                        super.updateItem(imageUrl, empty);
                        if (!empty) {
                            if (imageUrl == null || imageUrl.isEmpty()) {
                                // If imageUrl is null or empty, display default image
                                imageView.setImage(new Image("file:///C:/wamp64/www/images/nullimage.png"));
                            } else {
                                // Load the image from the provided URL
                                imageView.setImage(new Image("file:///C:/wamp64/www/" + imageUrl));
                            }
                            imageView.setFitWidth(70);  // Adjust the width
                            imageView.setFitHeight(70); // Adjust the height
                            setGraphic(imageView);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        caction.setCellFactory(new Callback<TableColumn<Espace, Void>, TableCell<Espace, Void>>() {
            @Override
            public TableCell<Espace, Void> call(TableColumn<Espace, Void> param) {
                return new TableCell<Espace, Void>() {
                    private final Button reserverButton = new Button("Reserver");
                    private final Button explorerButton = new Button("Explorer");

                    {
                        reserverButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        explorerButton.setStyle("-fx-background-color: #1d1593; -fx-text-fill: white;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Espace espace = getTableView().getItems().get(getIndex());

                            // Gérer le clic sur le bouton "Reserver"
                            reserverButton.setOnAction(event -> {
                                boolean reservationReussie = espaceService.reserverEspacePourEvenement(evenementId, espace.getId());
                                if (reservationReussie) {
                                    showAlert("Succès", "Espace réservé avec succès !", Alert.AlertType.INFORMATION);
                                    // Fermer la fenêtre après la réservation
                                    ((Stage) reserverButton.getScene().getWindow()).close();
                                } else {
                                    showAlert("Erreur", "Impossible de réserver cet espace.", Alert.AlertType.ERROR);
                                }
                            });

                            // Gérer le clic sur le bouton "Explorer"
                            explorerButton.setOnAction(event -> {
                                // Logique pour explorer l'espace (à implémenter)
                            });

                            setGraphic(new HBox(5, reserverButton, explorerButton));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        espaceTable.setItems(espaces);
        initData();
    }

    private void initData() {
        espaces.clear();

        // Fetch all the Espace objects
        List<Espace> espacesList = espaceService.rechercher();

        // Fetch all TypeEspace objects
        List<TypeEspace> typeEspaces = typeEspaceService.rechercher();

        // Loop through each Espace and find its corresponding TypeEspace
        for (Espace espace : espacesList) {
            for (TypeEspace typeEspace : typeEspaces) {
                if (typeEspace.getId() == espace.getTypeEspace().getId()) {
                    espace.setTypeEspace(typeEspace);  // Set the corresponding TypeEspace
                    break;  // Exit loop once matching TypeEspace is found
                }
            }
            espaces.add(espace);
        }

        // Now, set the items in the TableView
        espaceTable.setItems(espaces);
    }

    private void searchEspace(String searchText) {
        ObservableList<Espace> filteredList = FXCollections.observableArrayList();

        // Go through the original list of espaces and filter based on the search text
        for (Espace espace : espaces) {
            if (espace.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                    espace.getLocalisation().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(espace);
            }
        }

        // Create a new list that contains the original items but also preserves the buttons in the Action column
        espaceTable.setItems(filteredList);

        // Refresh the Action column (buttons) after filtering
        caction.setCellFactory(new Callback<TableColumn<Espace, Void>, TableCell<Espace, Void>>() {
            @Override
            public TableCell<Espace, Void> call(TableColumn<Espace, Void> param) {
                return new TableCell<Espace, Void>() {
                    private final Button reserverButton = new Button("Reserver");
                    private final Button explorerButton = new Button("Explorer");

                    {
                        reserverButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        explorerButton.setStyle("-fx-background-color: #1d1593; -fx-text-fill: white;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Espace espace = getTableView().getItems().get(getIndex());
                            reserverButton.setOnAction(event -> {
                                // Handle the 'Reserver' button action
                            });

                            explorerButton.setOnAction(event -> {
                                // Handle the 'Explorer' button action
                            });

                            setGraphic(new HBox(5, reserverButton, explorerButton));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void handleTri() {
        String selectedOption = ctri.getValue();
        if (selectedOption != null) {
            ObservableList<Espace> espaceList = FXCollections.observableArrayList(espaces);

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
                    initData();
                    return;
            }
            espaceTable.setItems(espaceList); // Update the table
        }
    }

    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void handleRecherche() {
        String searchText = tfrecherche.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Espace> filteredList = FXCollections.observableArrayList();
            for (Espace espace : espaces) { // Loop through the full list
                if (espace.getNom().toLowerCase().contains(searchText)) {
                    filteredList.add(espace);
                }
            }
            espaceTable.setItems(filteredList);
        } else {
            espaceTable.setItems(espaces);
        }
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
                    return;
            }
            espaceTable.setItems(espaceList); // Update the table
        }
    }

    public void recherche(ActionEvent actionEvent) {
        String searchText = tfrecherche.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Espace> filteredList = FXCollections.observableArrayList();
            for (Espace espace : espaces) {
                if (espace.getNom().toLowerCase().contains(searchText)) {
                    filteredList.add(espace);
                }
            }
            espaceTable.setItems(filteredList);
        } else {
            espaceTable.setItems(espaces);
        }
    }

    public void retour(javafx.event.ActionEvent actionEvent) {
        switchScene(actionEvent, "/InterfaceEspace/Homepage.fxml");
    }

    private void switchScene(javafx.event.ActionEvent actionEvent, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pdf(javafx.event.ActionEvent actionEvent) {
        try {
            String dest = "C:\\Users\\dell\\Desktop\\pdf\\espace\\GeneratedEspaceReport.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Liste des espaces"));
            for (Espace espace : espaceTable.getItems()) {
                document.add(new Paragraph("Nom de l'espace: " + espace.getNom()));
                document.add(new Paragraph("Localisation: " + espace.getLocalisation()));
                document.add(new Paragraph("État: " + espace.getEtat().toString()));
                document.add(new Paragraph("\n"));
            }
            document.close();
            showAlert("Success", "PDF generated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            // Handle error
            showAlert("Error", "An error occurred while generating the PDF.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void excel(javafx.event.ActionEvent actionEvent) {
        // File chooser for saving the Excel file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Espaces");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Nom", "Localisation", "État"};
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (Espace espace : espaceTable.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(espace.getNom());
                row.createCell(1).setCellValue(espace.getLocalisation());
                row.createCell(2).setCellValue(espace.getEtat().toString());
            }
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            showAlert("Success", "Excel file generated successfully!", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "Failed to generate Excel file.", Alert.AlertType.ERROR);
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

    public void stat(javafx.event.ActionEvent actionEvent) {
        int disponibleCount = 0;
        int indisponibleCount = 0;
        Map<String, Integer> typeEspaceCount = new HashMap<>();
        System.out.println("DEBUG: Checking Espace -> TypeEspace Mapping");
        var espaceService = new EspaceService();
        var espaces = espaceService.fetchAllEspaces();
        for (Espace espace : espaces) {
            if (espace.getTypeEspace() == null) {
                System.out.println("Error: TypeEspace is NULL for espace ID: " + espace.getId());
                continue;
            } else {
                System.out.println("Espace ID: " + espace.getId() + ", Type: " + espace.getTypeEspace().getType());
            }
            if (espace.getEtat() == Disponibilite.DISPONIBLE) {
                disponibleCount++;
            } else {
                indisponibleCount++;
            }
            String type = espace.getTypeEspace().getType();
            typeEspaceCount.put(type, typeEspaceCount.getOrDefault(type, 0) + 1);
        }
        int totalEspaces = disponibleCount + indisponibleCount;
        PieChart pieChartAvailability = new PieChart();
        PieChart.Data disponibleData = new PieChart.Data(
                "Disponible (" + calculatePercentage(disponibleCount, totalEspaces) + "%)", disponibleCount);
        PieChart.Data indisponibleData = new PieChart.Data(
                "Indisponible (" + calculatePercentage(indisponibleCount, totalEspaces) + "%)", indisponibleCount);
        pieChartAvailability.getData().addAll(disponibleData, indisponibleData);
        pieChartAvailability.setTitle("Disponibilité des Espaces");
        PieChart pieChartTypeEspace = new PieChart();
        for (Map.Entry<String, Integer> entry : typeEspaceCount.entrySet()) {
            int count = entry.getValue();
            pieChartTypeEspace.getData().add(
                    new PieChart.Data(entry.getKey() + " (" + calculatePercentage(count, totalEspaces) + "%)", count));
        }
        pieChartTypeEspace.setTitle("Utilisation des Types d'Espace");
        VBox vbox = new VBox(10, pieChartAvailability, pieChartTypeEspace);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Espaces");
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }

    private int calculatePercentage(int part, int total) {
        if (total == 0) return 0;
        return (int) Math.round((part * 100.0) / total);
    }

    public void qr(javafx.event.ActionEvent actionEvent) {
        Espace selectedEspace = espaceTable.getSelectionModel().getSelectedItem();
        if (selectedEspace == null) {
            showAlert("Error", "No row selected!", Alert.AlertType.ERROR);
            System.out.println("No row selected!");
            return;
        }
        String nom = selectedEspace.getNom();
        String localisation = selectedEspace.getLocalisation();
        String etat = selectedEspace.getEtat().toString();
        String imageUrl = selectedEspace.getimageUrl();
        TypeEspace typeEspace = selectedEspace.getTypeEspace();
        String type = (typeEspace != null && typeEspace.getType() != null) ? typeEspace.getType() : "N/A";
        String description = (typeEspace != null && typeEspace.getDescription() != null) ? typeEspace.getDescription() : "N/A";
        String qrContent = String.format("Nom: %s\nLocalisation: %s\nÉtat: %s\nType: %s\nDescription: %s\nImage URL: %s",
                nom, localisation, etat, type, description, imageUrl);
        Image qrImage = generateQRCode(qrContent, "PNG");
        if (qrImage != null) {
            System.out.println("QR Code generated successfully!");
            qrI.setImage(qrImage);
        } else {
            System.out.println("QR Code generation failed!");
        }
    }

    public Image generateQRCode(String text, String format) {
        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    pixelWriter.setColor(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return writableImage;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}