package controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import data_access.*;
import helpers.dialog_messages.DialogMessages;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import models.Service;
import models.Transaction;
import models.TransactionItem;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class POSController implements Initializable {

    @FXML
    private StackPane stackpane;

    @FXML
    private JFXComboBox<String> cbCategory;

    @FXML
    private TableView<Service> tvAllServices;

    @FXML
    private TableColumn<Service, Integer> colAllServicesId;

    @FXML
    private TableColumn<Service, String> colAllServicesName;

    @FXML
    private TableColumn<Service, Double> colAllServicesPrice;

    @FXML
    private TableColumn<Service, String> colAllServicesCategory;

    @FXML
    private JFXTextField tfAppId;

    @FXML
    private JFXTextField tfCustomerName;

    @FXML
    private TableView<Service> tvPurchaseServices;


    @FXML
    private TableColumn<Service, String> colPurchaseServicesName;

    @FXML
    private TableColumn<Service, Double> colPurchaseServicesPrice;

    @FXML
    private TableColumn<Service, String> colPurchaseServicesCategory;

    @FXML
    private TableColumn<Service, Integer> colPurchaseServicesServiceId;

    @FXML
    private JFXTextField tfTotalPrice;

    @FXML
    private JFXTextField tfVoucherCode;

    @FXML
    private JFXTextField tfVoucherDiscount;

    @FXML
    private JFXTextField tfFinalPrice;

    @FXML
    private JFXTextField tfCash;

    @FXML
    private JFXTextField tfBalance;

    @FXML
    private JFXButton btnProcess;

    @FXML
    private ImageView imgVoucher;

    private DataSource db = null;
    private Connection conn = null;
    private POSDAO posDao = null;
    private ServiceCategoryDAO serviceCategoryDAO = null;
    private ObservableList<Service> allServicesList = null;
    private ObservableList<Service> purchaseServicesList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InitialLoad();
            LoadServiceComboBoxData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        DisableVoucher();
        CalculateBalance();

        // FilterTVAllServicesByCategory();
    }

    public void FilterTVAllServicesByCategory() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Service> filteredData = new FilteredList<>(allServicesList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        cbCategory.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Service -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare name and description of every inventory with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Service.getCategory().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches name.
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Service> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tvAllServices.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tvAllServices.setItems(sortedData);

    }

    private void DisableVoucher() {
        tfTotalPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            double currentValue = 0.0;

            if (!(tfTotalPrice.getText().isEmpty())) {
                currentValue = Double.parseDouble(tfTotalPrice.getText());
            }

            if (currentValue > 0.0) {
                tfVoucherCode.setDisable(false);
            } else {
                tfVoucherCode.setDisable(true);
            }
        });
    }


    private void InitialLoad() throws SQLException {
        try {
            ShowAllServices();
            LoadServiceComboBoxData();
            ShowAllPurchase();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void ShowAllServices() throws SQLException {
        try {
            allServicesList = LoadAllServicesFromDB();
            colAllServicesId.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
            colAllServicesName.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));
            colAllServicesCategory.setCellValueFactory(new PropertyValueFactory<Service, String>("category"));
            colAllServicesPrice.setCellValueFactory(new PropertyValueFactory<Service, Double>("price"));
            tvAllServices.setItems(allServicesList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void ShowAllPurchase() {
        colPurchaseServicesServiceId.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
        colPurchaseServicesName.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));
        colPurchaseServicesCategory.setCellValueFactory(new PropertyValueFactory<Service, String>("category"));
        colPurchaseServicesPrice.setCellValueFactory(new PropertyValueFactory<Service, Double>("price"));
        tvPurchaseServices.setItems(purchaseServicesList);

    }

    private ObservableList<Service> LoadAllServicesFromDB() throws SQLException {
        ObservableList<Service> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            posDao = new POSDAO(conn);
            list = posDao.getAllServices();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (posDao != null)
                    posDao.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;

    }

    private void LoadServiceComboBoxData() throws SQLException {
        cbCategory.setItems(FXCollections.observableArrayList(loadServiceCategory()));
    }

    private List<String> loadServiceCategory() throws SQLException {
        List<String> list = FXCollections.observableArrayList();
        try {

            db = new DataSource();
            conn = db.getConnection();
            serviceCategoryDAO = new ServiceCategoryDAO(conn);
            list = serviceCategoryDAO.getAllServiceCategoryNames();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (serviceCategoryDAO != null)
            serviceCategoryDAO.close();
        ConnectionResources.close(conn);

        return list;
    }


    public void AppointmentSearch(MouseEvent mouseEvent) throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        int custId = 0;
        try {
            custId = Integer.parseInt(tfAppId.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            dm.InvalidAppointmentId();
            return;
        }
        DataSource db = new DataSource();
        conn = db.getConnection();
        posDao = new POSDAO(conn);
        String customerName = posDao.CheckAppointmentValidity(custId);

        if (customerName == null) {
            dm.NoAppointmentFound();
            return;
        } else {
            tfCustomerName.setText(customerName);
            EnableControlsToProcessTransactions();
        }

    }

    private void EnableControlsToProcessTransactions() {
        tvAllServices.setDisable(false);
        cbCategory.setDisable(false);
    }


    public void AllServicesTableDoubleCLicked(MouseEvent event) {
        Service model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvAllServices.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        AddServiceToPurchaseServiceTable(model);
        RemoveSelectedServiceFromAllServicesTableView(model);
    }

    private void RemoveSelectedServiceFromAllServicesTableView(Service model) {
        tvAllServices.getItems().removeAll(model);
        tvAllServices.refresh();
    }


    public void DeleteServiceFromPurchaseTable(KeyEvent event) {
        Service model = tvPurchaseServices.getSelectionModel().getSelectedItem();

        if (!(event.getCode() == KeyCode.DELETE) && model != null) {
            return;
        }
        RemoveServiceFromPurchaseTableTableView(model);
        AddServiceToAllServiceTableView(model);
    }

    private void AddServiceToPurchaseServiceTable(Service model) {
        purchaseServicesList.add(model);
        tvPurchaseServices.refresh();
        if (tvPurchaseServices.isDisable() == true) {
            tvPurchaseServices.setDisable(false);
        }
        updatePriceAdd(model);
    }

    private void updatePriceAdd(Service model) {
        double currentValue = 0.0;

        if (tfTotalPrice.getText().isEmpty() == false) {
            currentValue = Double.parseDouble(tfTotalPrice.getText());
        }

        double addedServicePrice = model.getPrice();
        double newValue = currentValue + addedServicePrice;
        tfTotalPrice.setText(String.valueOf(newValue));

        if (tfFinalPrice.getText().isEmpty() == false) {
            double currentFinalPrice = Double.parseDouble(tfFinalPrice.getText());
            double newFinalPrice = currentFinalPrice + addedServicePrice;
            tfFinalPrice.setText(String.valueOf(newFinalPrice));
            EnableCashField();
        } else {
            tfFinalPrice.setText(String.valueOf(newValue));
            EnableCashField();
        }


    }

    private void updatePriceSub(Service model) {
        double currentValue = 0.0;

        if (!(tfTotalPrice.getText().isEmpty())) {
            currentValue = Double.parseDouble(tfTotalPrice.getText());
        }

        double removedServicePrice = model.getPrice();
        double newValue = currentValue - removedServicePrice;

        if (newValue == 0.0) {
            tfTotalPrice.clear();
            tfFinalPrice.clear();
            RemoveExistingVoucher();
            DisableCashField();
            tfVoucherCode.setDisable(true);
            btnProcess.setDisable(true);
        } else {
            double currentFinalPrice = Double.parseDouble(tfFinalPrice.getText());
            double newFinalPrice = currentFinalPrice - removedServicePrice;
            tfTotalPrice.setText(String.valueOf(newValue));
            tfFinalPrice.setText(String.valueOf(newFinalPrice));
        }

    }


    private void AddServiceToAllServiceTableView(Service model) {
        allServicesList.add(model);
        tvAllServices.refresh();
    }

    private void RemoveServiceFromPurchaseTableTableView(Service model) {
        tvPurchaseServices.getItems().removeAll(model);
        tvPurchaseServices.refresh();
        updatePriceSub(model);
    }

    public void ApplyVoucher() throws SQLException {
        DialogMessages dm = new DialogMessages(stackpane);
        int voucher = 0;
        try {
            voucher = Integer.parseInt(tfVoucherCode.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            dm.RecheckVoucher();
            return;
        }
        DataSource db = new DataSource();
        conn = db.getConnection();
        posDao = new POSDAO(conn);
        int amount = posDao.CheckVoucherValidity(voucher);

        if (amount == 0) {
            dm.InvalidVoucher();
            return;
        } else {
            tfVoucherDiscount.setText(String.valueOf(amount));
            EnableRemoveVoucher();
            String vouchAmount = tfVoucherDiscount.getText();
            UpdateFinalPriceWithVoucher(vouchAmount);
        }

    }

    private void RemoveExistingVoucher() {
        UpdateFinalPriceWithoutVoucher();
        imgVoucher.setVisible(false);
        imgVoucher.setDisable(true);
        tfVoucherCode.clear();
        tfVoucherDiscount.clear();
        tfVoucherCode.setDisable(false);
    }


    private void UpdateFinalPriceWithVoucher(String amount) {
        double finalPrice = 0.0;
        if (tfTotalPrice.getText().isEmpty() == true) {
            return;
        }
        double vouchAmount = Double.parseDouble(amount);
        finalPrice = Double.parseDouble(tfTotalPrice.getText());

        if (finalPrice > vouchAmount) {
            finalPrice = finalPrice - vouchAmount;
            tfFinalPrice.setText(String.valueOf(finalPrice));
        }

    }

    private void UpdateFinalPriceWithoutVoucher() {
        double finalPrice = 0.0;
        if (tfFinalPrice.getText().isEmpty() == true) {
            return;
        }
        double vouchAmount = Double.parseDouble(tfVoucherDiscount.getText());
        finalPrice = Double.parseDouble(tfFinalPrice.getText());

        if (finalPrice > vouchAmount) {
            finalPrice = finalPrice + vouchAmount;
            tfFinalPrice.setText(String.valueOf(finalPrice));
        }
    }


    public void VoucherEntered(KeyEvent event) throws SQLException, FileNotFoundException {
        if (event.getCode() == KeyCode.ENTER) {
            ApplyVoucher();
        } else {
            return;
        }
    }

    public void RemoveVoucherClicked(MouseEvent mouseEvent) {
        RemoveExistingVoucher();
    }

    private void EnableRemoveVoucher() {
        imgVoucher.setVisible(true);
        imgVoucher.setDisable(false);
        tfVoucherCode.setDisable(true);
    }

    private void EnableCashField() {
        tfCash.setDisable(false);
    }

    private void DisableCashField() {
        tfCash.clear();
        tfCash.setDisable(true);
    }

    private void CalculateBalance() {
        tfCash.textProperty().addListener((observable, oldValue, newValue) -> {

            if (tfCash.getText().isEmpty() == true) {
                return;
            }
            if (tfFinalPrice.getText().isEmpty() == true) {
                return;
            }

            double finalPrice = Double.parseDouble(tfFinalPrice.getText());
            double cash = Double.parseDouble(tfCash.getText());

            if (finalPrice <= cash) {
                double balance = cash - finalPrice;
                tfBalance.setText(String.valueOf(balance));
                btnProcess.setDisable(false);
            }

        });

    }


    public void ProcessTransaction(ActionEvent event) {
        DialogMessages dm = new DialogMessages(stackpane);
        Transaction newTransactionData = new Transaction();
        double finalprice = Double.parseDouble(tfFinalPrice.getText());
        double cash = Double.parseDouble(tfCash.getText());

        if (purchaseServicesList.isEmpty()) {
            dm.EmptyPurchaseList();
            return;
        }
        if (finalprice > cash) {
            dm.InsuffientCashAmount();
            return;
        }

        List<TransactionItem> transactionItems = new ArrayList<>();
        //Get all purchase services
        for (int serviceItemIndex = 0; serviceItemIndex < purchaseServicesList.size(); serviceItemIndex++) {
            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setId(serviceItemIndex + 1);
            transactionItem.setService(purchaseServicesList.get(serviceItemIndex));
            transactionItems.add(transactionItem);
        }
        newTransactionData.setTransactionItems(transactionItems);

        //Get Current Date and Time
        Date date = java.util.Calendar.getInstance().getTime();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        java.sql.Time sqlTime = new java.sql.Time(date.getTime());

        //Add current Date and time Data to Transaction
        newTransactionData.setDate(sqlDate);
        newTransactionData.setTime(sqlTime);

        //Get appointment ID
        newTransactionData.setAppoinmentId(Integer.parseInt(tfAppId.getText()));

        if (tfVoucherCode.getText().isEmpty() == false) {
            int voucherCode = Integer.parseInt(tfVoucherCode.getText());
            newTransactionData.setVocuherId(voucherCode);
        }

        //Get cash Amount
        newTransactionData.setCash(Double.parseDouble(tfCash.getText()));

        //Get Balance Amount
        newTransactionData.setBalance(Double.parseDouble(tfBalance.getText()));

        try {
            db = new DataSource();
            conn = db.getConnection();
            posDao = new POSDAO(conn);

            Boolean isTransactionSuccess = false;
            isTransactionSuccess = posDao.CreateNewTransaction(newTransactionData);

            if (isTransactionSuccess == false) {
                SendTransactionCompleteSMS();
                dm.TransactionFailed();
                return;
            }
            dm.TransactionSuccessFul();
            ResetAllControls();


        } catch (SQLException ex) {
            ex.printStackTrace();
            dm.TransactionFailed();
        } finally {
            try {
                if (posDao != null) {
                    posDao.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    // TODO:SEND appointment success SMS
    private void SendTransactionCompleteSMS() {

    }

    private void ResetAllControls() {
        purchaseServicesList.clear();
        tvPurchaseServices.refresh();
        tvPurchaseServices.setDisable(true);
        tvAllServices.setDisable(true);
        cbCategory.setDisable(true);

        btnProcess.setDisable(true);
        DisableCashField();
        RemoveExistingVoucher();
        tfVoucherCode.clear();
        tfVoucherDiscount.clear();
        tfVoucherDiscount.setDisable(true);
        tfVoucherCode.setDisable(true);
        tfFinalPrice.clear();
        tfTotalPrice.clear();
        tfBalance.clear();

        tfAppId.clear();
        tfCustomerName.clear();

    }


}

