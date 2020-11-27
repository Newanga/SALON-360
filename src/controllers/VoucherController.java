package controllers;

import com.jfoenix.controls.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Voucher;
import validation.VoucherFormValidation;
import view_models.VoucherVM;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class VoucherController implements Initializable {


    @FXML
    private StackPane stackpane;

    @FXML
    private JFXTextField tfSearchTerm;

    @FXML
    private TableView<Voucher> tvVouchers;

    @FXML
    private TableColumn<Voucher, Integer> colID;

    @FXML
    private TableColumn<Voucher, Integer> colAmount;

    @FXML
    private TableColumn<Voucher, Date> colDateAdded;

    @FXML
    private TableColumn<Voucher, String> colState;

    @FXML
    private TableColumn<Voucher, String> colSpecialNotes;

    @FXML
    private JFXTextField tfID;

    @FXML
    private JFXTextField tfAmount;

    @FXML
    private JFXDatePicker dpDateAdded;

    @FXML
    private JFXComboBox<String> cbState;

    @FXML
    private JFXTextArea taSpecialNotes;

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private Label lblActiveVouchers;


    private DataSource db;
    private Connection conn;
    private VoucherDAO voucherDAO;
    private VoucherStateDAO voucherStateDAO;
    private ObservableList<Voucher> voucherList;
    private Voucher voucherModel = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void InitialLoad() throws SQLException {
        LoadVoucherComboBoxData();
        ShowVouchers();
        DefaultFormValues();
        LoadDashBoardData();
    }

    private void LoadDashBoardData() {
        VoucherVM voucherVM;

        try {
            DataSource db = new DataSource();
            conn = db.getConnection();
            voucherDAO = new VoucherDAO(conn);
            voucherVM = voucherDAO.getDashBoardData();
            lblActiveVouchers.setText(String.valueOf(voucherVM.getActiveVouchers()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void DefaultFormValues() {
        SetDateColumn();
        SetCBStateValue();
    }

    private void ShowVouchers() {
        voucherList = LoadVouchersFromDB();
        colID.setCellValueFactory(new PropertyValueFactory<Voucher, Integer>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<Voucher, Integer>("amount"));
        colDateAdded.setCellValueFactory(new PropertyValueFactory<Voucher, Date>("dateAdded"));
        colSpecialNotes.setCellValueFactory(new PropertyValueFactory<Voucher, String>("specialNotes"));
        colState.setCellValueFactory(new PropertyValueFactory<Voucher, String>("state"));
        tvVouchers.setItems(voucherList);
    }


    private void SetDateColumn() {
        dpDateAdded.setValue(LocalDate.now());
    }

    private void SetCBStateValue() {
        cbState.setValue("Valid");
    }


    private void LoadVoucherComboBoxData() {
        try {
            cbState.setItems(FXCollections.observableArrayList(loadVoucherStates()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> loadVoucherStates() {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            voucherStateDAO = new VoucherStateDAO(conn);
            list = voucherStateDAO.getAllVoucherStateNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            //Close SQL Resources
            if (voucherStateDAO != null)
                voucherStateDAO.close();
            ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }


    private ObservableList<Voucher> LoadVouchersFromDB() {
        ObservableList<Voucher> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            voucherDAO = new VoucherDAO(conn);
            list = voucherDAO.getAllVouchers();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (voucherDAO != null)
                    voucherDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;
    }

    public void btnCreateClicked(ActionEvent actionEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try {
            db = new DataSource();
            conn = db.getConnection();
            voucherDAO = new VoucherDAO(conn);

            voucherModel = new Voucher();

            voucherModel.setState(cbState.getValue());
            voucherModel.setSpecialNotes(taSpecialNotes.getText());

            //Validate Amount
            String amount = tfAmount.getText();
            Boolean validPrice = VoucherFormValidation.ValidateAmount(amount);
            if (!validPrice) {
                dm.InvalidAmount();
                return;
            } else {
                voucherModel.setAmount(Integer.parseInt(amount));
            }

            //Get Date from date picker
            Date date = Date.from(dpDateAdded.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            voucherModel.setDateAdded(sqlDate);


            Boolean result = voucherDAO.CreateNewVoucher(voucherModel);

            if (result == true)
                dm.InsertSuccessDialogBox();
            else
                dm.InsertFailedDialogBox();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        clearTextFieldsAndComboBoxes();
        InitialLoad();
        btnUpdate.setDisable(true);
        btnCreate.setDisable(false);

        try {
            //closing connection resources
            if (voucherDAO != null)
                voucherDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void btnUpdateClicked(ActionEvent actionEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        try {
            db = new DataSource();
            conn = db.getConnection();
            voucherDAO = new VoucherDAO(conn);

            voucherModel = new Voucher();

            voucherModel.setId(Integer.parseInt(tfID.getText()));
            voucherModel.setState(cbState.getValue());
            voucherModel.setSpecialNotes(taSpecialNotes.getText());

            //Validate Amount
            String amount = tfAmount.getText();
            Boolean validPrice = VoucherFormValidation.ValidateAmount(amount);
            if (!validPrice) {
                dm.InvalidAmount();
                return;
            } else {
                voucherModel.setAmount(Integer.parseInt(amount));
            }

//            //Get Date from date picker
//            Date date = Date.from(dpDateAdded.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
//            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//            voucherModel.setDateAdded(sqlDate);

            try {
                Date date = Date.from(dpDateAdded.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                voucherModel.setDateAdded(sqlDate);
            } catch (Exception ex) {
                dm.InvalidDate();
                return;
            }


            Boolean result = voucherDAO.UpdateVoucher(voucherModel);

            if (result == true)
                dm.UpdateSuccessDialogBox();
            else
                dm.UpdateFailedDialogBox();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        clearTextFieldsAndComboBoxes();
        btnUpdate.setDisable(true);
        btnCreate.setDisable(false);
        InitialLoad();

        try {
            //closing connection resources
            if (voucherDAO != null)
                voucherDAO.close();
            if (conn != null)
                ConnectionResources.close(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void btnKeyClicked(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            tvVouchers.getSelectionModel().clearSelection();
            clearTextFieldsAndComboBoxes();
            btnCreate.setDisable(false);
            btnUpdate.setDisable(true);
            LoadVoucherComboBoxData();
            DefaultFormValues();
        }

    }

    public void btnExcelExportClicked(MouseEvent mouseEvent) {
        ExportToExcel ex = new ExportToExcel(tvVouchers, stackpane);
        ex.run();
    }

    public void SearchFunction(MouseEvent mouseEvent) {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Voucher> filteredData = new FilteredList<>(voucherList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearchTerm.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Voucher -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every category with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(Voucher.getId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Voucher> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvVouchers.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvVouchers.setItems(sortedData);
    }

    public void tvMouseClicked(MouseEvent event) {
        Voucher model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvVouchers.getSelectionModel().getSelectedItem();
        } else {
            return;
        }

        //validates whether the selected object(row) is null or not null
        if (model == null) {
            return;
        } else {
            cbState.setValue(model.getState());
            tfID.setText(String.valueOf(model.getId()));
            tfAmount.setText(String.valueOf(model.getAmount()));
            dpDateAdded.setValue(model.getDateAdded().toLocalDate());
            taSpecialNotes.setText(model.getSpecialNotes());
            btnCreate.setDisable(true);
            btnUpdate.setDisable(false);
        }
    }

    public void clearTextFieldsAndComboBoxes() {
        tfID.clear();
        tfAmount.clear();
        tfSearchTerm.clear();
        taSpecialNotes.clear();
        dpDateAdded.getEditor().clear();
        dpDateAdded.setValue(null);
        cbState.setValue(null);
    }


}
