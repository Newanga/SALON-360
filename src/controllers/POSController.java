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

    private DataSource db ;
    private Connection conn ;
    private POSDAO posDao;
    private ServiceCategoryDAO serviceCategoryDAO ;
    private ObservableList<Service> allServicesList;
    private ObservableList<Service> purchaseServicesList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initialLoad();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        disableVoucher();
        CalculateBalance();

    }


    private void disableVoucher() {
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


    private void initialLoad() throws SQLException {
        try {
            showAllServices();
            showAllPurchase();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void showAllServices() throws SQLException {
        try {
            allServicesList = loadAllServicesFromDB();
            colAllServicesId.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
            colAllServicesName.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));
            colAllServicesCategory.setCellValueFactory(new PropertyValueFactory<Service, String>("category"));
            colAllServicesPrice.setCellValueFactory(new PropertyValueFactory<Service, Double>("price"));
            tvAllServices.setItems(allServicesList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void showAllPurchase() {
        colPurchaseServicesServiceId.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
        colPurchaseServicesName.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));
        colPurchaseServicesCategory.setCellValueFactory(new PropertyValueFactory<Service, String>("category"));
        colPurchaseServicesPrice.setCellValueFactory(new PropertyValueFactory<Service, Double>("price"));
        tvPurchaseServices.setItems(purchaseServicesList);

    }

    private ObservableList<Service> loadAllServicesFromDB() throws SQLException {
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




    public void appointmentSearch(MouseEvent mouseEvent) throws SQLException {
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
            enableControlsToProcessTransactions();
        }

    }

    private void enableControlsToProcessTransactions() {
        tvAllServices.setDisable(false);
    }


    public void allServicesTableDoubleCLicked(MouseEvent event) {
        Service model = null;

        //check for a double click on table to load to object
        if (event.getClickCount() == 2) {
            model = tvAllServices.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        addServiceToPurchaseServiceTable(model);
        removeSelectedServiceFromAllServicesTableView(model);
    }

    private void removeSelectedServiceFromAllServicesTableView(Service model) {
        tvAllServices.getItems().removeAll(model);
        tvAllServices.refresh();
    }


    public void deleteServiceFromPurchaseTable(KeyEvent event) {
        Service model = tvPurchaseServices.getSelectionModel().getSelectedItem();

        if (!(event.getCode() == KeyCode.DELETE) && model != null) {
            return;
        }
        RemoveServiceFromPurchaseTableTableView(model);
        addServiceToAllServiceTableView(model);
    }

    private void addServiceToPurchaseServiceTable(Service model) {
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
            removeExistingVoucher();
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


    private void addServiceToAllServiceTableView(Service model) {
        allServicesList.add(model);
        tvAllServices.refresh();
    }

    private void RemoveServiceFromPurchaseTableTableView(Service model) {
        tvPurchaseServices.getItems().removeAll(model);
        tvPurchaseServices.refresh();
        updatePriceSub(model);
    }

    public void applyVoucher() throws SQLException {
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

    private void removeExistingVoucher() {
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
            applyVoucher();
        } else {
            return;
        }
    }

    public void RemoveVoucherClicked(MouseEvent mouseEvent) {
        removeExistingVoucher();
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
            newTransactionData.setVoucherId(voucherCode);
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


    private void ResetAllControls() throws SQLException {
        purchaseServicesList = FXCollections.observableArrayList();
        tvPurchaseServices.refresh();
        tvPurchaseServices.setDisable(true);
        tvAllServices.setDisable(true);
        btnProcess.setDisable(true);

        //clear voucher
        imgVoucher.setVisible(false);
        imgVoucher.setDisable(true);
        tfVoucherCode.clear();
        tfVoucherDiscount.clear();
        tfVoucherCode.setDisable(true);


        tfCash.clear();
        tfCash.setDisable(true);
        tfFinalPrice.clear();
        tfFinalPrice.setDisable(true);
        tfTotalPrice.clear();
        tfFinalPrice.setDisable(true);
        tfBalance.clear();
        tfFinalPrice.setDisable(true);

        tfAppId.clear();
        tfCustomerName.clear();

        showAllServices();
        showAllPurchase();

    }

    public void btnKeyClicked(KeyEvent event) throws SQLException {

        if (event.getCode() == KeyCode.ESCAPE) {
            ResetAllControls();
        }

    }


}

