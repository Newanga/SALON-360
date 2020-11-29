package controllers;

import data_access.*;
import helpers.jasper_reports.CreateTransactionReport;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;


import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import models.Service;
import view_models.tables.TransactionVM;

public class TransactionController implements Initializable {


    @FXML
    private StackPane stackpane;

    @FXML
    private JFXDatePicker dpSearchFrom;

    @FXML
    private JFXDatePicker dpSearchTo;

    @FXML
    private TableView<TransactionVM> tvTransactions;

    @FXML
    private TableColumn<TransactionVM, Integer> colId;

    @FXML
    private TableColumn<TransactionVM, Integer> colAppointmentId;

    @FXML
    private TableColumn<TransactionVM, Date> colDate;

    @FXML
    private TableColumn<TransactionVM, Double> colAmount;

    @FXML
    private TableColumn<TransactionVM, Double> colCash;

    @FXML
    private TableColumn<TransactionVM, Double> colBalance;

    @FXML
    private TableColumn<TransactionVM, Integer> colDiscount;

    private DataSource db;
    private Connection conn;
    private TransactionsDAO transactionDAO;
    private ObservableList<TransactionVM> transactionList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitialLoad();
    }

    public void InitialLoad() {
        ShowTransactions();
    }

    private void ShowTransactions() {
        transactionList = LoadTransactionsFromDatabase();
        colId.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("transactionId"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("appointmentId"));
        colDate.setCellValueFactory(new PropertyValueFactory<TransactionVM, Date>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("amount"));
        colBalance.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("balance"));
        colCash.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("cash"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("discount"));
        tvTransactions.setItems(transactionList);

    }

    private ObservableList<TransactionVM> LoadTransactionsFromDatabase() {
        ObservableList<TransactionVM> list = FXCollections.observableArrayList();

        try {
            db = new DataSource();
            conn = db.getConnection();
            transactionDAO = new TransactionsDAO(conn);
            list = transactionDAO.getAllTransactions();
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                //closing connection resources
                if (transactionDAO != null)
                    transactionDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return list;
    }


    public void GenerateReport(ActionEvent event) {
        CreateTransactionReport report = new CreateTransactionReport(transactionList, "TransactionsReport", stackpane);
        report.run();

    }


    //Todo : Date Range Filter
    private void FilterBasedOnDateRange() {



    }
}
