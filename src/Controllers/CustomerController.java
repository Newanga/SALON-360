package Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class CustomerController {

    @FXML
    private StackPane stackpane;


    @FXML
    private Label lblCTotalCustomers;
    @FXML
    private Label lblCActiveCustomers;
    @FXML
    private Label lblCInActiveCustomers;

    @FXML
    private TableView tvCustomer;
    @FXML
    private TableColumn colCId;
    @FXML
    private TableColumn colCFirstName;
    @FXML
    private TableColumn colCLastName;
    @FXML
    private TableColumn colCAddress;
    @FXML
    private TableColumn colCContactNo;
    @FXML
    private TableColumn colCEmail;
    @FXML
    private TableColumn colCDOB;
    @FXML
    private TableColumn colCGender;
    @FXML
    private TableColumn colCState;

    @FXML
    private JFXTextField tfCId;
    @FXML
    private JFXTextField tfCFirstName;
    @FXML
    private JFXTextField tfCLastName;
    @FXML
    private JFXTextField tfCAddress;
    @FXML
    private JFXTextField tfCContactNo;
    @FXML
    private JFXTextField tfCEmail;
    @FXML
    private JFXDatePicker dpCDOB;
    @FXML
    private JFXComboBox cbCGender;
    @FXML
    private JFXComboBox cbCState;


}
