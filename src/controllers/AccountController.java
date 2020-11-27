package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import data_access.*;
import helpers.dialog_messages.DialogMessages;
import helpers.report_generation.ExportToExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Account;
import validation.AccountFormValidation;
import view_models_dashboard.AccountVM;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private StackPane stackpane;

    @FXML
    private Label lblTotalAccounts;

    @FXML
    private Label lblActiveAccounts;

    @FXML
    private Label lblInActiveAccounts;


    @FXML
    private JFXTextField tfSearchTerm;

    @FXML
    private TableView<Account> tvAccounts;

    @FXML
    private TableColumn<Account, Integer> colId;

    @FXML
    private TableColumn<Account, String> colFirstName;

    @FXML
    private TableColumn<Account, String> colLastName;

    @FXML
    private TableColumn<Account, String> colRole;

    @FXML
    private TableColumn<Account, String> colUsername;

    @FXML
    private TableColumn<Account, String> colPassword;

    @FXML
    private TableColumn<Account, String> colState;

    @FXML
    private JFXTextField tfId;

    @FXML
    private JFXTextField tfFirstName;

    @FXML
    private JFXTextField tfLastName;

    @FXML
    private JFXComboBox<String> cbRole;

    @FXML
    private JFXTextField tfUsername;

    @FXML
    private JFXTextField tfPassword;

    @FXML
    private JFXComboBox<String> cbState;

    @FXML
    private JFXButton btnUpdate;

    private DataSource db;
    private Connection conn;
    private EmployeeRoleDAO employeeRoleDAO;
    private EmployeeStateDAO employeeStateDAO;
    private ObservableList<Account> accountList;
    private AccountDAO accountDAO;
    private Account accountModel = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void InitialLoad() throws SQLException {
        LoadAccountComboBoxData();
        ShowAccounts();
        LoadDashBoardData();
    }

    private void LoadDashBoardData() throws SQLException {
        AccountVM accountVM;
        try{
            db = new DataSource();
            conn = db.getConnection();
            accountDAO = new AccountDAO(conn);
            accountVM=accountDAO.getDashBoardData();
            lblActiveAccounts.setText(String.valueOf(accountVM.getActiveAccounts()));
            lblInActiveAccounts.setText(String.valueOf(accountVM.getInactiveAccounts()));
            lblTotalAccounts.setText(String.valueOf(accountVM.getTotalAccounts()));
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }


    private void LoadAccountComboBoxData() {
        try {
            cbState.setItems(FXCollections.observableArrayList(loadEmployeeStates()));
            cbRole.setItems(FXCollections.observableArrayList(loadRoles()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> loadRoles() {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            employeeRoleDAO = new EmployeeRoleDAO(conn);
            list = employeeRoleDAO.getAllEmployeeRoleNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            //Close SQL Resources
            if (employeeRoleDAO != null)
                employeeRoleDAO.close();
            ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private List<String> loadEmployeeStates() {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            employeeStateDAO = new EmployeeStateDAO(conn);
            list = employeeStateDAO.getAllEmployeeStateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            //Close SQL Resources
            if (employeeStateDAO != null)
                employeeStateDAO.close();
            ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private void ShowAccounts() {
        accountList = LoadAccountsFromDB();
        colId.setCellValueFactory(new PropertyValueFactory<Account, Integer>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<Account, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Account, String>("lastName"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Account, String>("password"));
        colState.setCellValueFactory(new PropertyValueFactory<Account, String>("state"));
        colRole.setCellValueFactory(new PropertyValueFactory<Account, String>("role"));
        tvAccounts.setItems(accountList);
    }

    private ObservableList<Account> LoadAccountsFromDB() {
        ObservableList<Account> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            accountDAO = new AccountDAO(conn);
            list = accountDAO.getAllAccounts();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (accountDAO != null)
                    accountDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;
    }

    private void clearSTextFieldsAndComboBoxes() {
        tfId.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfSearchTerm.clear();
        tfUsername.clear();
        tfPassword.clear();;
        cbState.setValue(null);
        cbRole.setValue(null);
    }

    public void btnUpdateClicked() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try{
            db = new DataSource();
            conn = db.getConnection();
            accountDAO=new AccountDAO(conn);
            accountModel = new Account();

            accountModel.setId(Integer.parseInt(tfId.getText()));
            accountModel.setFirstName(tfFirstName.getText());
            accountModel.setLastName(tfLastName.getText());
            accountModel.setState(cbState.getValue());
            accountModel.setRole(cbRole.getValue());
            accountModel.setUsername(tfUsername.getText());
            accountModel.setPassword(tfPassword.getText());

            //Check for empty data
            boolean isDataEmpty= AccountFormValidation.validateEmptyData(accountModel);
            if(isDataEmpty){
                dm.EmptyDataInForm();
                return;
            }

//            boolean isUsernameValid= AccountFormValidation.validateUsername(accountModel.getUsername());
//            if(!isUsernameValid){
//                dm.InvalidUsername();
//                return;
//            }

//            boolean isPassswordValid= AccountFormValidation.validatePassword(accountModel.getUsername());
//            if(!isPassswordValid){
//                dm.InvalidPassword();
//                return;
//            }


            Boolean result = accountDAO.UpdateAccount(accountModel);

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
        tfPassword.setDisable(true);
        tfUsername.setDisable(true);

        try{
            //closing connection resources
            if (accountDAO != null)
                accountDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void tvMouseClicked(MouseEvent event) {
        Account model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvAccounts.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            cbState.setValue(model.getState());
            cbRole.setValue(model.getRole());
            tfId.setText(String.valueOf(model.getId()));
            tfFirstName.setText(model.getFirstName());
            tfLastName.setText(model.getLastName());
            tfUsername.setText(model.getUsername());
            tfPassword.setText(model.getPassword());
            btnUpdate.setDisable(false);
            tfUsername.setDisable(false);
            tfPassword.setDisable(false);
        }
    }

    public void btnKeyClicked(KeyEvent event) {

        if (event.getCode() == KeyCode.ESCAPE) {
            tvAccounts.getSelectionModel().clearSelection();
            clearSTextFieldsAndComboBoxes();
            btnUpdate.setDisable(true);
            tfUsername.setDisable(true);
            tfPassword.setDisable(true);
            LoadAccountComboBoxData();
        }

    }

    public void btnSExcelExportClicked(MouseEvent mouseEvent) {
        ExportToExcel ex = new ExportToExcel(tvAccounts, stackpane);
        ex.run();

    }

    public void SearchFunction() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Account> filteredData = new FilteredList<>(accountList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Account -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Account.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                }else if (String.valueOf(Account.getLastName()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches price.
                } else if (String.valueOf(Account.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Account> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvAccounts.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvAccounts.setItems(sortedData);

    }
}
