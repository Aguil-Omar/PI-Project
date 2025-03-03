package com.pi_dev.controllers.CommentController;

import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;
import com.pi_dev.services.SentimentAnalyzerService;
import com.pi_dev.services.obsceneLanguageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.services.commentaireService;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class CommentCellController extends ListCell<commentaire> {

    @FXML private ImageView userPicture;
    @FXML private Label UserLabel;
    @FXML private Label commentLabel;
    @FXML private Label dateLabel;
    @FXML private Button likeButton;
    @FXML private Button dislikeButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Label commentIdLabel;
    @FXML private Label sentimentLabel;

     private Utilisateur loggedUser;
     private commentaire currentComment;


    public void setCommentData(commentaire comment) {
        this.currentComment = comment;
        AdminService AD= AdminService.getInstance();
        this.loggedUser = AD.getLoggedUser();
        AdminService adminService = new AdminService();
        Utilisateur user = adminService.getUserById(comment.getUtilisateur_id());
        updateButtonStyles();
        if(user != null) {
            UserLabel.setText(user.getNom()+" "+user.getPrenom());
            this.setUserPicture(user.getImageUrl());
        }else{
            UserLabel.setText("eventopia user");
            this.setUserPicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8xUq147l_TTS9xI4z07sj8xuxNcJ3CuFNsg&s");
        }

        if (loggedUser != null && loggedUser.getId() == comment.getUtilisateur_id()) {
            deleteButton.setVisible(true);
            updateButton.setVisible(true);
        } else {
            deleteButton.setVisible(false);
            updateButton.setVisible(false);
        }


        commentIdLabel.setText(String.valueOf(comment.getId()));
        commentLabel.setText(comment.getComment());
        dateLabel.setText(comment.getTime_comment().toString()+" | "+comment.getDate_comment().toString());
        likeButton.setText("\uD83D\uDC4D  "+comment.getNbr_Likes());
        dislikeButton.setText("\uD83D\uDC4E  "+ comment.getNbr_Dislikes());


        String sentiment = SentimentAnalyzerService.analyzeSentiment(comment.getComment());

        sentimentLabel.setText(sentiment);
        if ("positive".equals(sentiment)) {
            sentimentLabel.setStyle("-fx-text-fill: green;");
        } else if ("negative".equals(sentiment)) {
            sentimentLabel.setStyle("-fx-text-fill: red;");
        } else {
            sentimentLabel.setStyle("-fx-text-fill: gray;");
        }
    }

    public void setUserPicture(String imagePath) {
        try {

            Image image = new Image(imagePath);
            userPicture.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            Image image = new Image("https://cdn-icons-png.flaticon.com/512/219/219983.png");
            userPicture.setImage(image);
        }}


    public void deleteComment(ActionEvent actionEvent) {
        commentaireService commentaireService = new commentaireService();
        int id = Integer.parseInt(commentIdLabel.getText());
        commentaireService.supprimer(new commentaire(id, commentLabel.getText()));
        ListView<commentaire> listView = (ListView<commentaire>) deleteButton.getScene().lookup("#commentListView");
        listView.getItems().removeIf(comment -> comment.getId() == id);
    }





    public void openUpdatePopup(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceComment/updateComment.fxml"));
            Parent root = loader.load();
            UpdateCommentController updateCommentController = loader.getController();
            updateCommentController.initializeCommentData(new commentaire(Integer.parseInt(commentIdLabel.getText()), commentLabel.getText()));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Comment");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void updateButtonStyles() {
        // Check if the user has already liked or disliked the comment
        commentaireService cs = new commentaireService();
        String interaction = cs.getUserInteraction(loggedUser.getId(), currentComment.getId());

        if ("LIKE".equals(interaction)) {
            likeButton.setStyle("-fx-background-color: #0078B8FF; -fx-text-fill: #CEF1F9;");
            dislikeButton.setStyle("-fx-background-color: grey;");
        } else if ("DISLIKE".equals(interaction)) {
            dislikeButton.setStyle("-fx-background-color: #EF5151FF; -fx-text-fill: #CEF1F9;");
            likeButton.setStyle("-fx-background-color: grey;");
        } else {
            likeButton.setStyle("-fx-background-color: grey;");
            dislikeButton.setStyle("-fx-background-color: grey;");
        }
    }

    public void handleLike(ActionEvent actionEvent) {
        commentaireService cs = new commentaireService();
        cs.handleLikeDislike(loggedUser.getId(), currentComment.getId(), "LIKE");
        commentaire updatedComment = cs.getCommentById(this.currentComment.getId());
        likeButton.setText("👍 " + updatedComment.getNbr_Likes());
        dislikeButton.setText("👎 " + updatedComment.getNbr_Dislikes());
        updateButtonStyles();
    }

    public void handleDislike(ActionEvent actionEvent) {
        commentaireService cs = new commentaireService();
        cs.handleLikeDislike(loggedUser.getId(), currentComment.getId(), "DISLIKE");
        commentaire updatedComment = cs.getCommentById(this.currentComment.getId());
        likeButton.setText("👍 " + updatedComment.getNbr_Likes());
        dislikeButton.setText("👎 " + updatedComment.getNbr_Dislikes());
        updateButtonStyles();
    }
}





