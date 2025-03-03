package com.pi_dev.controllers.CommentController;
import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.models.Forum.avis;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.commentaireService;
import com.pi_dev.services.avisService;

import com.pi_dev.services.obsceneLanguageService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.pi_dev.models.GestionEvenement.evenement;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Comparator;


public class CommentController {

    @FXML private Label errorLabel;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private AnchorPane rootPane;
    @FXML private Text star1;
    @FXML private Text star2;
    @FXML private Text star3;
    @FXML private Text star4;
    @FXML private Text star5;
    @FXML private Label avgRateLabel;
    @FXML private TextField txComment;
    @FXML private ListView<commentaire> commentListView;


    private int rate = 0;
    private evenement currentEvenement;



    private static CommentController instance;
    private Utilisateur loggedUser;
    public CommentController() {
        instance = this;
    }
    public static CommentController getInstance() {
        return instance;
    }

    @FXML
    public void addComment(javafx.event.ActionEvent actionEvent) {


        if (txComment.getText().trim().isEmpty()) {
            errorLabel.setText("Please enter a comment.");
            errorLabel.setVisible(true);
            return;
        }
        if (obsceneLanguageService.containsObsceneLanguage(txComment.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Inappropriate Language");
            alert.setHeaderText(null);
            alert.setContentText(this.loggedUser.getNom() +" ,your comment contains inappropriate language. Please modify it.");
            alert.showAndWait();

        }else {
            errorLabel.setVisible(false);
            commentaireService cs = new commentaireService();
            cs.ajouter(new commentaire(txComment.getText()), this.currentEvenement,this.loggedUser.getId());
            txComment.setText("");
            this.initialize(this.currentEvenement);
        }
    }
    @FXML
    public void initialize(evenement event) {
        AdminService AD= AdminService.getInstance();
        this.loggedUser = AD.getLoggedUser();
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "Date ▲",
                "Date ▼"
        );
        sortComboBox.setItems(sortingOptions);
        sortComboBox.getSelectionModel().selectFirst();
        this.currentEvenement = event;
        errorLabel.setVisible(false);
        rootPane.getStylesheets().add(getClass().getResource("/styles/comment.css").toExternalForm());
        updateAverageRating();
        commentaireService cs = new commentaireService();
        ObservableList<commentaire> comments= FXCollections.observableArrayList(cs.rechercher(this.currentEvenement));
        System.out.println(comments);
        commentListView.setItems(comments);


        commentListView.setCellFactory(listView -> new ListCell<commentaire>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(commentaire comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null) {
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/InterfaceComment/cellComment.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    CommentCellController controller = loader.getController();
                    controller.setCommentData(comment);
                }
            }
        });
    }
    public void load() {
        commentaireService cs = new commentaireService();
        ObservableList<commentaire> comments= FXCollections.observableArrayList(cs.rechercher(this.currentEvenement));
        commentListView.setItems(comments);
    }
    public void handleSortComboBox(ActionEvent actionEvent) {
        String selectedSort = sortComboBox.getSelectionModel().getSelectedItem();
        ObservableList<commentaire> comments = commentListView.getItems();

        if ("Date ▲".equals(selectedSort)) {
            comments.sort(Comparator.comparing(commentaire::getDate_comment)); // Sort by date in ascending order
        } else if ("Date ▼".equals(selectedSort)) {
            comments.sort(Comparator.comparing(commentaire::getDate_comment).reversed()); // Sort by date in descending order
        }

        commentListView.setItems(comments);
    }

 //-----------------------------------------------avis-------------------------------------------


    @FXML
    private void rate1() {
        rate = 1;
        updateStars();
        saveRatingToDatabase();
        updateAverageRating();
    }
    @FXML
    private void rate2() {
        rate = 2;
        updateStars();
        saveRatingToDatabase();
        updateAverageRating();
    }
    @FXML
    private void rate3() {
        rate = 3;
        updateStars();
        saveRatingToDatabase();
        updateAverageRating();
    }
    @FXML
    private void rate4() {
        rate = 4;
        updateStars();
        saveRatingToDatabase();
        updateAverageRating();
    }
    @FXML
    private void rate5() {
        rate = 5;
        updateStars();
        saveRatingToDatabase();
        updateAverageRating();
    }
    private void updateStars() {
        star1.setStyle("-fx-fill: grey;");
        star2.setStyle("-fx-fill: grey;");
        star3.setStyle("-fx-fill: grey;");
        star4.setStyle("-fx-fill: grey;");
        star5.setStyle("-fx-fill: grey;");


        if (rate >= 1) star1.setStyle("-fx-fill: gold;");
        if (rate >= 2) star2.setStyle("-fx-fill: gold;");
        if (rate >= 3) star3.setStyle("-fx-fill: gold;");
        if (rate >= 4) star4.setStyle("-fx-fill: gold;");
        if (rate >= 5) star5.setStyle("-fx-fill: gold;");
    }
    private void saveRatingToDatabase() {
        avisService as =new avisService();
        int idUser=this.loggedUser.getId();
        if(as.getAvisByUserAndEvent(idUser,this.currentEvenement)!=null){
            avis avisExist=as.getAvisByUserAndEvent(idUser,this.currentEvenement);
            avis av=new avis(avisExist.getId(),this.rate);
            System.out.println("enter to modifie avis");
            as.modifier(av);
            showSuccessPopup("Rating Updated Successfully!");
        }else{
            System.out.println("enter to ajouter avis");
            avis a =new avis(this.rate,idUser,this.currentEvenement.getId());
            as.ajouter(a);
            showSuccessPopup("Rating Added Successfully!");
        }
        updateStars();
        updateAverageRating();
    }
    private void updateAverageRating() {
        avisService as = new avisService();
        double moyenne = as.calculerMoyenneNotes(this.currentEvenement);
        avgRateLabel.setText(String.format("Avg Of rates: %.1f", moyenne));
    }
    private void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            alert.close();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

}
