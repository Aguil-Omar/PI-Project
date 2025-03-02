package com.pi_dev.controllers.CommentController;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class CommentCellController extends ListCell<commentaire> {
    @FXML private Label commentLabel;
    @FXML private Label dateLabel;
    @FXML private Button likeButton;
    @FXML private Button deleteButton;
    @FXML private Label commentIdLabel;




    public void setCommentData(commentaire comment) {

        commentIdLabel.setText(String.valueOf(comment.getId()));
        commentLabel.setText(comment.getComment());
        dateLabel.setText(comment.getTime_comment().toString()+" | "+comment.getDate_comment().toString());
        likeButton.setText("\uD83D\uDC4D  "+comment.getNbr_Likes());
    }


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
}





