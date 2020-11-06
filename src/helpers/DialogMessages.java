package helpers;

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
        close.setStyle("-fx-background-color: #EF5350");
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
        close.setStyle("-fx-background-color: #EF5350");
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

    public void EmptyDataInForm() {
        String title ="Operation Failed";
        String content="Some or all of the fields contain missing data.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
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

    public void NoDataToExport(){
        String title ="Export Failed";
        String content="No Data in table to Export.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
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

    public void ExportSuccessful(){
        String title ="Export Successful";
        String content="Find the exported data at the saved location.";
        dialogContent= new JFXDialogLayout();
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

    public void InvalidPrice() {
        String title ="Invalid Price";
        String content="Price should be a value less than 10000.00";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
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

    public void InvalidDate() {
        String title ="Invalid Date";
        String content="Please select a date from date picker or enter a valid date in the format MM/DD/YYYY";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
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

    public void InvalidContactNo() {
        String title ="Invalid ContactNo";
        String content="The contact No should only contain 10 digits.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
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

    public void InvalidEmail() {
        String title ="Invalid Email";
        String content="Please recheck the entered email address.";
        dialogContent= new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
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
