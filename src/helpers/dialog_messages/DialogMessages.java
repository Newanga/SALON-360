package helpers.dialog_messages;

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

    public void InsertSuccessDialogBox() { ;
        String title = "Operation successful";
        String content = "The new record was created successfully.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InsertFailedDialogBox() {
        String title = "Operation Failed";
        String content = "The record was not created.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void UpdateSuccessDialogBox() {
        String title = "Operation successful";
        String content = "The record was updated successfully;";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void UpdateFailedDialogBox() {
        String title = "Operation Failed";
        String content = "The record was not Updated.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void EmptyDataInForm() {
        String title = "Operation Failed";
        String content = "Some or all of the fields contain missing data.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoDataToExport() {
        String title = "Export Failed";
        String content = "No Data in table to Export.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void ExportSuccessful() {
        String title = "Export Successful";
        String content = "Find the exported data at the saved location.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidPrice() {
        String title = "Invalid Price";
        String content = "Price should be a value less than 10000.00";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidDate() {
        String title = "Invalid Date";
        String content = "Please select a date from date picker or enter a valid date in the format MM/DD/YYYY";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidContactNo() {
        String title = "Invalid ContactNo";
        String content = "The contact No should only contain 10 digits.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidEmail() {
        String title = "Invalid Email";
        String content = "Please recheck the entered email address.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidQuantity() {
        String title = "Invalid Quantity";
        String content = "Quantity should be greater than 1 and max of 100";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #EF5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidUsername() {
        String title = "Invalid Username";
        String content = "Please recheck Username";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidPassword() {

    }

    public void InvalidAmount() {
        String title = "Invalid Amount";
        String content = "Amount should be min of 100 and max of 1000";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoMessageModeSelected() {
        String title = "No Message mode selected";
        String content = "Please select a message mode and fill required details of the selected mode";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoCustomerDataAvailable() {
        String title = "No Data Available";
        String content = "Please Add customers to send bulk messages";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoMessageData() {
        String title = "No Message Data";
        String content = "Please Select a message template and add a common message to send";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoReceiverCustomerSelected() {
        String title = "No Receiver selected";
        String content = "Please Select a receiver customer from the right customer table";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void MessageDeliveredSuccessfully() {
        String title = "Message Delivered Successfully";
        String content = "The message was delivered.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidTime() {
        String title = "Invalid Time";
        String content = "Pick a time from the time picker";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidCustomerId() {
        String title = "Invalid Customer Id";
        String content = "Please enter a valid CustomerId";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }
    public void UnableToFindCustomer() {
        String title = "Invalid Customer Id";
        String content = "Unable To find Customer";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void FoundCustomer() {
        String title = "Operation successful";
        String content = "Customer Found";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #00bfff");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void SelectAppointmentState() {
        String title = "AppointmentState Missing";
        String content = "Select customer appointment state.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }


    public void InvalidAppointmentId() {
        String title = "Invalid Appointment Id";
        String content = "Please enter a valid appointment Id";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InvalidVoucher() {
        String title = "Invalid Voucher";
        String content = "Voucher is not available.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void RecheckVoucher() {
        String title = "Invalid Voucher Format";
        String content = "Please enter a valid voucher Id";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void EmptyPurchaseList() {
        String title = "Empty purchase list";
        String content = "Add atleast a single purchase item to proceed the transaction.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void InsuffientCashAmount() {
        String title = "Insuffient Cash Amount";
        String content = "Change the cash amount to a higher value.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void NoAppointmentFound() {
        String title = "Appointment unavailable";
        String content = "No Valid Appointment Found For Appointment Id.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }

    public void TransactionSuccessFul() {
        String title = "Failed";
        String content = "Transaction failed. Please Try Again.";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();

    }

    public void TransactionFailed() {
        String title = "Successful";
        String content = "Transaction was successful";
        dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(content));
        JFXButton close = new JFXButton("Close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color: #ef5350");
        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        dialog.show();
    }
}
