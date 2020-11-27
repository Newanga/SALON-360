package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import data_access.*;
import helpers.dialog_messages.DialogMessages;
import helpers.report_generation.ExportToExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Employee;
import validation.EmployeeFormValidation;
import view_models.EmployeeVM;


import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private StackPane stackpane;


    @FXML
    private Label lblTotalEmployees;

    @FXML
    private Label lblActiveEmployees;

    @FXML
    private Label lblInactiveEmployees;

    @FXML
    private JFXTextField tfSearchTerm;

    @FXML
    private TableView<Employee> tvEmployees;

    @FXML
    private TableColumn<Employee, Integer> colId;

    @FXML
    private TableColumn<Employee, String> colFirstName;

    @FXML
    private TableColumn<Employee, String> colLastName;

    @FXML
    private TableColumn<Employee, String> colAddress;

    @FXML
    private TableColumn<Employee, String> colRole;

    @FXML
    private TableColumn<Employee, String> colContactNo;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, Date> colDOB;

    @FXML
    private TableColumn<Employee, String> colGender;

    @FXML
    private TableColumn<Employee, String> colState;

    @FXML
    private JFXTextField tfId;

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
    private JFXComboBox<String> cbRole;

    @FXML
    private JFXComboBox<String> cbGender;

    @FXML
    private JFXComboBox<String> cbState;

    @FXML
    private ImageView imageview;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnUpdate;


    private DataSource db;
    private Connection conn;
    private EmployeeDAO employeeDAO;
    private EmployeeRoleDAO employeeRoleDAO;
    private EmployeeStateDAO employeeStateDAO;
    private GenderDAO genderDAO;
    private ObservableList<Employee> employeeList;
    private Employee employeeModel = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void InitialLoad() throws SQLException {
        LoadEmployeeComboBoxData();
        ShowEmployees();
        LoadDashBoardData();
    }

    private void LoadDashBoardData(){
        EmployeeVM employeeVM=new EmployeeVM();
        try{
            db = new DataSource();
            conn = db.getConnection();
            employeeDAO = new EmployeeDAO(conn);
            employeeVM=employeeDAO.getDashBoardData();
            lblTotalEmployees.setText(String.valueOf(employeeVM.getTotalEmployees()));
            lblActiveEmployees.setText(String.valueOf(employeeVM.getActiveEmployees()));
            lblInactiveEmployees.setText(String.valueOf(employeeVM.getInactiveEmployees()));

        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private void LoadEmployeeComboBoxData() {
        try {
            cbState.setItems(FXCollections.observableArrayList(loadEmployeeStates()));
            cbGender.setItems(FXCollections.observableArrayList(loadGenders()));
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

    private List<String> loadGenders() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            genderDAO = new GenderDAO(conn);
            list = genderDAO.getAllGenderSNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            //Close SQL Resources
            if (genderDAO != null)
                genderDAO.close();
            ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private void ShowEmployees() throws SQLException {
        employeeList = LoadEmployeesFromDB();
        colId.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<Employee, String>("contactNo"));
        colGender.setCellValueFactory(new PropertyValueFactory<Employee, String>("gender"));
        colState.setCellValueFactory(new PropertyValueFactory<Employee, String>("state"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        colDOB.setCellValueFactory(new PropertyValueFactory<Employee, Date>("dob"));
        colRole.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));
        tvEmployees.setItems(employeeList);
    }

    public ObservableList<Employee> LoadEmployeesFromDB() throws SQLException {
        ObservableList<Employee> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            employeeDAO = new EmployeeDAO(conn);
            list = employeeDAO.getAllEmployees();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (employeeDAO != null)
                    employeeDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;

    }

    public void tvMouseClicked(MouseEvent event) throws SQLException {
        Employee model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvEmployees.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            cbGender.setValue(model.getGender());
            cbState.setValue(model.getState());
            cbRole.setValue(model.getRole());
            tfId.setText(String.valueOf(model.getId()));
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
        tfId.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfEmail.clear();
        tfContactNo.clear();
        tfAddress.clear();
        tfSearchTerm.clear();
        dpDOB.getEditor().clear();
        dpDOB.setValue(null);
        cbState.setValue(null);
        cbGender.setValue(null);
        cbRole.setValue(null);
    }

    public void btnUpdateClicked() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try{
            db = new DataSource();
            conn = db.getConnection();
            employeeDAO=new EmployeeDAO(conn);

            employeeModel = new Employee();

            //
            employeeModel.setId(Integer.parseInt(tfId.getText()));
            employeeModel.setFirstName(tfFirstName.getText());
            employeeModel.setLastName(tfLastName.getText());
            employeeModel.setAddress(tfAddress.getText());
            employeeModel.setEmail(tfEmail.getText());
            employeeModel.setContactNo(tfContactNo.getText());

            //check fo empty data column
            try{
                Date date = Date.from(dpDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                employeeModel.setDob(sqlDate);
            }catch (Exception ex){
                dm.InvalidDate();
                return;
            }
            employeeModel.setGender(cbGender.getValue());
            employeeModel.setState(cbState.getValue());
            employeeModel.setRole(cbRole.getValue());

            //Check for empty data
            boolean isDataEmpty= EmployeeFormValidation.validateEmptyData(employeeModel);
            if(isDataEmpty){
                dm.EmptyDataInForm();
                return;
            }

            //check for valid contact no
            boolean isValid=EmployeeFormValidation.validateContactNo(employeeModel.getContactNo());

            if(!isValid){
                dm.InvalidContactNo();
                return;
            }

            //check email
            boolean isEMailValid=EmployeeFormValidation.ValidateEmail(employeeModel.getEmail());

            if(!isEMailValid){
                dm.InvalidEmail();
                return;
            }

            Boolean result = employeeDAO.UpdateEmployee(employeeModel);

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
            if (employeeDAO != null)
                employeeDAO.close();
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
            employeeDAO=new EmployeeDAO(conn);

            employeeModel = new Employee();

            employeeModel.setFirstName(tfFirstName.getText());
            employeeModel.setLastName(tfLastName.getText());
            employeeModel.setAddress(tfAddress.getText());
            employeeModel.setEmail(tfEmail.getText());
            employeeModel.setContactNo(tfContactNo.getText());

            //check fo empty data column
            try{
                Date date = Date.from(dpDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                employeeModel.setDob(sqlDate);
            }catch (Exception ex){
                dm.InvalidDate();
                return;
            }
            employeeModel.setGender(cbGender.getValue());
            employeeModel.setState(cbState.getValue());
            employeeModel.setRole(cbRole.getValue());


            //Check for empty data
            boolean isDataEmpty= EmployeeFormValidation.validateEmptyData(employeeModel);
            if(isDataEmpty){
                dm.EmptyDataInForm();
                return;
            }

            //check for valid contact no
            boolean isValid=EmployeeFormValidation.validateContactNo(employeeModel.getContactNo());

            if(!isValid){
                dm.InvalidContactNo();
                return;
            }

            //check email
            boolean isEMailValid=EmployeeFormValidation.ValidateEmail(employeeModel.getEmail());

            if(!isEMailValid){
                dm.InvalidEmail();
                return;
            }

            Boolean result = employeeDAO.CreateEmployee(employeeModel);

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
            if (employeeDAO != null)
                employeeDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnKeyClicked(KeyEvent event) throws SQLException {

        if (event.getCode() == KeyCode.ESCAPE) {
            tvEmployees.getSelectionModel().clearSelection();
            clearSTextFieldsAndComboBoxes();
            btnCreate.setDisable(false);
            btnUpdate.setDisable(true);
            LoadEmployeeComboBoxData();
        }

    }

    public void btnSExcelExportClicked(MouseEvent mouseEvent) {
        ExportToExcel ex = new ExportToExcel(tvEmployees, stackpane);
        ex.run();
    }

    public void SearchFunction() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Employee> filteredData = new FilteredList<>(employeeList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Employee -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Employee.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                }else if (Employee.getLastName().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Employee.getContactNo()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else if (String.valueOf(Employee.getEmail()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(Employee.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Employee> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvEmployees.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvEmployees.setItems(sortedData);

    }



}
