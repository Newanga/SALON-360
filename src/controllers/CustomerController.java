package controllers;

import data_access.*;
import helpers.DialogMessages;
import helpers.Export;
import models.Customer;
import validation.CustomerFormValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private StackPane stackpane;

    @FXML
    private JFXTextField tfSearchTerm;

    @FXML
    private TableView<Customer> tvCustomer;

    @FXML
    private TableColumn<Customer, Integer> colID;

    @FXML
    private TableColumn<Customer, String> colFirstName;

    @FXML
    private TableColumn<Customer, String> colLastName;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colContactNo;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, Date> colDOB;

    @FXML
    private TableColumn<Customer, String> colGender;

    @FXML
    private TableColumn<Customer, String> colState;

    @FXML
    private JFXTextField tfID;

    @FXML
    private JFXTextField tfFirstName;

    @FXML
    private JFXTextField tfLastName;

    @FXML
    private JFXTextField tfAddress;

    @FXML
    private JFXTextField tfContactNo;

    @FXML
    private JFXTextField tfEmail;

    @FXML
    private JFXDatePicker dpDOB;

    @FXML
    private JFXComboBox<String> cbGender;

    @FXML
    private JFXComboBox<String> cbCustomerState;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnUpdate;

    private DataSource db;
    private Connection conn;
    private CustomerStateDAO cutomerStatedao;
    private GenderDAO genderdao;
    private CustomerDAO customerdao;
    private ObservableList<Customer> customersList;
    private Customer customerModel = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void InitialLoad() throws SQLException {
        LoadCustomerComboBoxData();
        ShowCustomers();
    }

    private void ShowCustomers() throws SQLException {
        customersList = LoadCustomersFromDB();
        colID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<Customer, String>("contactNo"));
        colGender.setCellValueFactory(new PropertyValueFactory<Customer, String>("gender"));
        colState.setCellValueFactory(new PropertyValueFactory<Customer, String>("state"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        colDOB.setCellValueFactory(new PropertyValueFactory<Customer, Date>("dob"));
        tvCustomer.setItems(customersList);
    }

    public ObservableList<Customer> LoadCustomersFromDB() throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            customerdao = new CustomerDAO(conn);
            list = customerdao.getAllCustomers();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (customerdao != null)
                    customerdao.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;

    }

    public void LoadCustomerComboBoxData() throws SQLException {
        try{
            cbCustomerState.setItems(FXCollections.observableArrayList(loadCustomerStates()));
            cbGender.setItems(FXCollections.observableArrayList(loadGenders()));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private List<String> loadGenders() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            genderdao = new GenderDAO(conn);
            list = genderdao.getAllGenderSNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            //Close SQL Resources
            if (genderdao != null)
                genderdao.close();
            ConnectionResources.close(conn);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return list;
    }

    private List<String> loadCustomerStates() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            cutomerStatedao = new CustomerStateDAO(conn);
            list = cutomerStatedao.getAllCustomerStateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try{
            //Close SQL Resources
            if (customerdao != null)
                customerdao.close();
            ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return list;
    }

    public void tvMouseClicked(MouseEvent event) {
        Customer model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 1) {
            model = tvCustomer.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            cbGender.setValue(model.getGender());
            cbCustomerState.setValue(model.getState());
            tfID.setText(String.valueOf(model.getId()));
            tfFirstName.setText(model.getFirstName());
            tfLastName.setText(model.getLastName());
            tfAddress.setText(model.getAddress());
            tfContactNo.setText(model.getContactNo());
            tfEmail.setText(model.getEmail());
            dpDOB.setValue(model.getDob().toLocalDate());
            btnCreate.setDisable(true);
            btnUpdate.setDisable(false);
        }
    }

    public void clearSTextFieldsAndComboBoxes() {
        tfID.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfEmail.clear();
        tfContactNo.clear();
        tfAddress.clear();
        tfSearchTerm.clear();
        dpDOB.getEditor().clear();
        dpDOB.setValue(null);
        cbCustomerState.setValue(null);
        cbGender.setValue(null);
    }

    public void btnUpdateClicked() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try{
            db = new DataSource();
            conn = db.getConnection();
            customerdao=new CustomerDAO(conn);

            customerModel = new Customer();

            //
            customerModel.setId(Integer.parseInt(tfID.getText()));
            customerModel.setFirstName(tfFirstName.getText());
            customerModel.setLastName(tfLastName.getText());
            customerModel.setAddress(tfAddress.getText());
            customerModel.setEmail(tfEmail.getText());
            customerModel.setContactNo(tfContactNo.getText());

            //check fo empty date column
            try{
                Date date = Date.from(dpDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                customerModel.setDob(sqlDate);
            }catch (Exception ex){
                dm.InvalidDate();
                return;
            }

            customerModel.setGender(cbGender.getValue());
            customerModel.setState(cbCustomerState.getValue());

            //Check for empty data
            boolean isDataEmpty=CustomerFormValidation.validateEmptyData(customerModel);
            if(isDataEmpty){
                dm.EmptyDataInForm();
                return;
            }

            //check for valid contact no
            boolean isValid=CustomerFormValidation.validateContactNo(customerModel.getContactNo());

            if(!isValid){
                dm.InvalidContactNo();
                return;
            }

            //check email
            boolean isEMailValid=CustomerFormValidation.ValidateEmail(customerModel.getEmail());

            if(!isEMailValid){
                dm.InvalidEmail();
                return;
            }

            Boolean result = customerdao.UpdateCustomer(customerModel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        clearSTextFieldsAndComboBoxes();
        InitialLoad();
        btnUpdate.setDisable(true);
        btnCreate.setDisable(false);

        try{
            //closing connection resources
            if (customerdao != null)
                customerdao.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnCreateClicked(ActionEvent actionEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try{
            db = new DataSource();
            conn = db.getConnection();
            customerdao=new CustomerDAO(conn);

            customerModel = new Customer();

            customerModel.setFirstName(tfFirstName.getText());
            customerModel.setLastName(tfLastName.getText());
            customerModel.setAddress(tfAddress.getText());
            customerModel.setEmail(tfEmail.getText());
            customerModel.setContactNo(tfContactNo.getText());

            //check fo empty data column
            try{
                Date date = Date.from(dpDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                customerModel.setDob(sqlDate);
            }catch (Exception ex){
                dm.InvalidDate();
                return;
            }
            customerModel.setGender(cbGender.getValue());
            customerModel.setState(cbCustomerState.getValue());

            //Check for empty data
            boolean isDataEmpty=CustomerFormValidation.validateEmptyData(customerModel);
            if(isDataEmpty){
                dm.EmptyDataInForm();
                return;
            }

            //check for valid contact no
            boolean isValid=CustomerFormValidation.validateContactNo(customerModel.getContactNo());

            if(!isValid){
                dm.InvalidContactNo();
                return;
            }

            //check email
            boolean isEMailValid=CustomerFormValidation.ValidateEmail(customerModel.getEmail());

            if(!isEMailValid){
                dm.InvalidEmail();
                return;
            }

            Boolean result = customerdao.CreateCustomer(customerModel);

            if (result == true)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        clearSTextFieldsAndComboBoxes();
        InitialLoad();
        btnUpdate.setDisable(true);
        btnCreate.setDisable(false);

        try{
            //closing connection resources
            if (customerdao != null)
                customerdao.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnKeyClicked(KeyEvent event) throws SQLException {

        if (event.getCode() == KeyCode.ESCAPE) {
            tvCustomer.getSelectionModel().clearSelection();
            clearSTextFieldsAndComboBoxes();
            btnCreate.setDisable(false);
            btnUpdate.setDisable(true);
            LoadCustomerComboBoxData();
        }

    }

    public void btnSExcelExportClicked(MouseEvent mouseEvent) {
        Export ex = new Export(tvCustomer, stackpane);
        ex.run();

    }

    public void SearchFunction() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Customer> filteredData = new FilteredList<>(customersList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Customer -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Customer.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                }else if (String.valueOf(Customer.getLastName()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches price.
                } else if (String.valueOf(Customer.getContactNo()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches price.
                }else if (String.valueOf(Customer.getEmail()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches price.
                } else if (String.valueOf(Customer.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Customer> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvCustomer.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvCustomer.setItems(sortedData);

    }
}
