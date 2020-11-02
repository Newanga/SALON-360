package Helpers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogMessages {
    private JFXDialogLayout dialogContent;
    private StackPane stackpane;

    public DialogMessages(StackPane stackpane) {
        this.stackpane = stackpane;
    }

    public void InsertSuccessDialogBox(){
        String title ="Operation successful";
        String content="The new record was created successfully.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane,dialogContent,JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InsertFailedDialogBox(){
        String title ="Operation Failed";
        String content="The record was not created.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #8f041b");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane,dialogContent,JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void UpdateSuccessDialogBox() {
        String title ="Operation successful";
        String content="The record was updated successfully;";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane,dialogContent,JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void UpdateFailedDialogBox() {
        String title ="Operation Failed";
        String content="The record was not Updated.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #8f041b");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane,dialogContent,JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }
}
