package com.pi_dev.controllers.CommentController;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.services.commentaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class UpdateCommentController {
    @FXML private TextField txUpdatedComment;
    @FXML private Label errorLabel;

    private commentaireService commentaireService = new commentaireService();
    private commentaire currentComment;

    public void initializeCommentData(commentaire comment) {
        txUpdatedComment.setText(comment.getComment());
        currentComment=comment;
    }

    public void updateComment(ActionEvent actionEvent) {
        if (currentComment != null) {
            String newCommentText = txUpdatedComment.getText();
            if (newCommentText.isEmpty()) {
                errorLabel.setText("Please enter a comment.");
                errorLabel.setVisible(true);
                return;
            }
            errorLabel.setVisible(false);
            currentComment.setComment(newCommentText);
            commentaireService.modifier(currentComment);
            CommentController.getInstance().load();
            ((Button) actionEvent.getSource()).getScene().getWindow().hide();
        }
    }


}
