package controllers;

import data_access.*;
import helpers.dialog_messages.DialogMessages;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Appointment;

public class AppointmentController implements Initializable {


    //////////////////////////////////////////////
    // Appointments //
    //////////////////////////////////////////////


    @FXML
    private StackPane stackpane;

    @FXML
    private JFXDatePicker dpAppSearchDate;

    @FXML
    private JFXTimePicker tpAppSearchTime;

    @FXML
    private JFXButton btnAppSearch;

    @FXML
    private TableView<Appointment> tvAppointments;

    @FXML
    private TableColumn<Appointment, Integer> colAppId;

    @FXML
    private TableColumn<Appointment, Date> colAppDate;

    @FXML
    private TableColumn<Appointment, String> colAppCustName;

    @FXML
    private TableColumn<Appointment, Time> colAppTime;

    @FXML
    private TableColumn<Appointment, String> colAppState;

    @FXML
    private JFXTextField tfAppCustId;

    @FXML
    private JFXTextField tfAppId;

    @FXML
    private JFXTextField tfCustName;

    @FXML
    private JFXDatePicker dpAPPDate;

    @FXML
    private JFXTimePicker tpAppTime;

    @FXML
    private JFXComboBox<String> cbAppState;

    @FXML
    private JFXButton btnAppCreate;

    @FXML
    private JFXButton btnAppUpdate;


    private DataSource db;
    private Connection conn;
    private AppointmentDAO appointmentDAO;
    private AppointmentStateDAO appointmentStateDAO;
    private ObservableList<Appointment> appointmentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void InitialLoad() throws SQLException {
        try {
            ShowAllAppointmentsToday();
            LoadAppointmentComboBoxData();
            ShowALlAppointments();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    private void LoadAppointmentComboBoxData() {
        try {
            cbAppState.setItems(FXCollections.observableArrayList(loadAppointmentStates()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> loadAppointmentStates() {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            appointmentStateDAO = new AppointmentStateDAO(conn);
            list = appointmentStateDAO.getAllAppointmentStateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            //Close SQL Resources
            if (appointmentStateDAO != null)
                appointmentStateDAO.close();
            ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public void ShowAllAppointmentsToday() throws SQLException {
        try {
            appointmentList = null;
            appointmentList = LoadAppointmentsTodayFromDB();
            colAppId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
            colAppCustName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("CustomerName"));
            colAppDate.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("AppointmentDate"));
            colAppTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("AppointmentTime"));
            colAppState.setCellValueFactory(new PropertyValueFactory<Appointment, String>("State"));
            tvAppointments.setItems(appointmentList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    public ObservableList<Appointment> LoadAppointmentsTodayFromDB() throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
            list = appointmentDAO.getAllAppointmentsToday();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (appointmentDAO != null)
                    appointmentDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;
    }


    public void btnAppSearchClicked(ActionEvent actionEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        java.sql.Date sqlDate;
        Time sqlTime;

        try {
            Date date = Date.from(dpAppSearchDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            sqlDate = new java.sql.Date(date.getTime());
        } catch (Exception ex) {
            dm.InvalidDate();
            return;
        }
        try {
            LocalTime time = tpAppSearchTime.getValue();
            sqlTime = Time.valueOf(time);
        } catch (Exception ex) {
            dm.InvalidTime();
            return;
        }
        ShowAllFilteredAppointments(sqlDate, sqlTime);

    }

    public void ShowAllFilteredAppointments(java.sql.Date sqlDate, Time sqlTime) throws SQLException {
        try {
            appointmentList = null;
            appointmentList = LoadAppointmentsFilteredFromDB(sqlDate, sqlTime);
            colAppId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
            colAppCustName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("CustomerName"));
            colAppDate.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("AppointmentDate"));
            colAppTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("AppointmentTime"));
            colAppState.setCellValueFactory(new PropertyValueFactory<Appointment, String>("State"));
            tvAppointments.setItems(null);
            tvAppointments.setItems(appointmentList);
            tvAppointments.refresh();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    public ObservableList<Appointment> LoadAppointmentsFilteredFromDB(java.sql.Date sqlDate, Time sqlTime) throws SQLException {

        ObservableList<Appointment> list = FXCollections.observableArrayList();
        try {
            db = new DataSource();
            conn = db.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
            list = appointmentDAO.getAllAppointmentsByDateAndTime(sqlDate, sqlTime);
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (appointmentDAO != null)
                appointmentDAO.close();
        }
        return list;
    }

    public void EnterKeyPressed(KeyEvent keyEvent) throws Exception {
        DialogMessages dm = new DialogMessages(stackpane);
        int custId = 0;
        if (keyEvent.getCode() == KeyCode.ENTER) {
            try {
                custId = Integer.parseInt(tfAppCustId.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                dm.InvalidCustomerId();
                return;
            }
            DataSource db = new DataSource();
            conn = db.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(conn);
            String customerName = customerDAO.getCustomerNameById(custId);

            if (customerName == null) {
                dm.UnableToFindCustomer();
                return;
            } else {
                dm.FoundCustomer();
                tfCustName.setText(customerName);
                EnableControlsCreateNewAppointment();
            }
        }

    }

    private void EnableControlsCreateNewAppointment() {
        tfAppCustId.setDisable(true);
        dpAPPDate.setDisable(false);
        tpAppTime.setDisable(false);
        cbAppState.setDisable(false);
        btnAppCreate.setDisable(false);
    }

    public void CreateNewAppointment(ActionEvent actionEvent) {
        DialogMessages dm = new DialogMessages(stackpane);
        Appointment appointment = new Appointment();
        appointment.setCustomerId(Integer.parseInt(tfAppCustId.getText()));

        //GEt Date
        try {
            Date date = Date.from(dpAPPDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            appointment.setAppointmentDate(sqlDate);
        } catch (Exception ex) {
            dm.InvalidDate();
            return;
        }

        //GetTime
        try {
            LocalTime time = tpAppTime.getValue();
            Time sqlTime = Time.valueOf(time);
            appointment.setAppointmentTime(sqlTime);
        } catch (Exception ex) {
            dm.InvalidTime();
            return;
        }

        if (cbAppState.getValue() == null) {
            dm.SelectAppointmentState();
            return;
        } else
            appointment.setState(cbAppState.getValue());

        // GEt Today Date in sql
        Date date = java.util.Calendar.getInstance().getTime();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        appointment.setBookedDate(sqlDate);

        try {
            DataSource db = new DataSource();
            conn = db.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
            Boolean result = appointmentDAO.CreateNewAppointment(appointment);

            if (result == true)
                //TODO: Send Appointment SMS
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();

            ClearAndResetAllControls();
            btnAppUpdate.setDisable(true);
            btnAppCreate.setDisable(false);
            InitialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (appointmentDAO != null)
                    appointmentDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void UpdateAppointment(ActionEvent actionEvent) {
        DialogMessages dm = new DialogMessages(stackpane);
        Appointment appointment = new Appointment();
        appointment.setId(Integer.parseInt(tfAppId.getText()));

        //GEt Date
        try {
            Date date = Date.from(dpAPPDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            appointment.setAppointmentDate(sqlDate);
        } catch (Exception ex) {
            dm.InvalidDate();
            return;
        }

        //GetTime
        try {
            LocalTime time = tpAppTime.getValue();
            Time sqlTime = Time.valueOf(time);
            appointment.setAppointmentTime(sqlTime);
        } catch (Exception ex) {
            dm.InvalidTime();
            return;
        }

        if (cbAppState.getValue() == null) {
            dm.SelectAppointmentState();
            return;
        } else
            appointment.setState(cbAppState.getValue());


        try {
            DataSource db = new DataSource();
            conn = db.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
            Boolean result = appointmentDAO.UpdateAppointment(appointment);

            if (result == true)
                // TODO : Appointment Update SMS
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

            ClearAndResetAllControls();
            btnAppUpdate.setDisable(true);
            btnAppCreate.setDisable(false);
            InitialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (appointmentDAO != null)
                    appointmentDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void ClearAndResetAllControls() {
        tfAppCustId.clear();
        tfAppId.clear();
        tfCustName.clear();
        dpAPPDate.getEditor().clear();
        tpAppTime.getEditor().clear();
        dpAppSearchDate.getEditor().clear();
        tpAppSearchTime.getEditor().clear();
        cbAppState.setValue(null);

        tfAppCustId.setDisable(false);
        tfCustName.setDisable(true);
        tfAppId.setDisable(true);
        dpAPPDate.setDisable(true);
        tpAppTime.setDisable(true);
        cbAppState.setDisable(true);

        }

    public void LoadDataFromtvToForm(MouseEvent event) {
        String custName = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            custName = tvAppointments.getSelectionModel().getSelectedItem().getCustomerName();
        } else {
            return;
        }
        //validates whether the selected object(row) is null or not null
        if (custName == null) {
            return;
        } else {
            tfAppId.setText(String.valueOf(tvAppointments.getSelectionModel().getSelectedItem().getId()));
            tfCustName.setText(tvAppointments.getSelectionModel().getSelectedItem().getCustomerName());
            dpAPPDate.setValue((tvAppointments.getSelectionModel().getSelectedItem().getAppointmentDate()).toLocalDate());
            tpAppTime.setValue((tvAppointments.getSelectionModel().getSelectedItem().getAppointmentTime()).toLocalTime());
            cbAppState.setValue(tvAppointments.getSelectionModel().getSelectedItem().getState());


            tfAppCustId.setDisable(true);
            dpAPPDate.setDisable(false);
            tpAppTime.setDisable(false);
            cbAppState.setDisable(false);
            btnAppCreate.setDisable(true);
            btnAppUpdate.setDisable(false);

        }
    }

    public void ClearAndResetAll(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
           ClearAndResetAllControls();
           btnAppUpdate.setDisable(true);
           btnAppCreate.setDisable(false);
        }
    }


    //////////////////////////////////////////////
    // All appointment //
    //////////////////////////////////////////////


    @FXML
    private JFXTextField tfAllAppIdSearch;

    @FXML
    private JFXTextField tfAllAppcCustIdSearch;

    @FXML
    private JFXDatePicker dpAllAppFromSearch;

    @FXML
    private JFXDatePicker dpAllAppToSearch;

    @FXML
    private TableView<Appointment> tvAllAppointments;

    @FXML
    private TableColumn<Appointment, Integer> colAllAppId;

    @FXML
    private TableColumn<Appointment, String> colAllCustName;

    @FXML
    private TableColumn<Appointment, Date> colAllAppBookedDate;

    @FXML
    private TableColumn<Appointment, Date> colAllAppDate;

    @FXML
    private TableColumn<Appointment, Time> colAllAppTime;

    @FXML
    private TableColumn<Appointment, String> colAllAppState;

    private ObservableList<Appointment> allAppointmentList;


    private void ShowALlAppointments() {
        try {
            allAppointmentList = null;
            allAppointmentList = LoadAllAppointmentsTFromDB();
            colAllAppId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
            colAllCustName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("CustomerName"));
            colAllAppBookedDate.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("BookedDate"));
            colAllAppDate.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("AppointmentDate"));
            colAllAppTime.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("AppointmentTime"));
            colAllAppState.setCellValueFactory(new PropertyValueFactory<Appointment, String>("State"));
            tvAllAppointments.setItems(allAppointmentList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Appointment> LoadAllAppointmentsTFromDB() throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
            list = appointmentDAO.getAllAppointments();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (appointmentDAO != null)
                    appointmentDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;
    }

    public void SearchALLAppointmentsById() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Appointment> filteredData = new FilteredList<>(allAppointmentList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfAllAppIdSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Appointment -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(Appointment.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Appointment> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvAllAppointments.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvAllAppointments.setItems(sortedData);

    }

    public void SearchALLAppointmentsByCustomerName() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Appointment> filteredData = new FilteredList<>(allAppointmentList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfAllAppcCustIdSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Appointment -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Appointment.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Appointment> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvAllAppointments.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvAllAppointments.setItems(sortedData);

    }

    //TODO:filter based on date range
    public void SearchALLAppointmentsBasedOnDateRange() {


    }

}


