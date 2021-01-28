package controllers;

import com.jfoenix.controls.JFXDatePicker;
import data_access.*;
import helpers.exports.ExportToExcel;
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

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import models.Appointment;
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
        initialLoad();
    }

    public void initialLoad() {
        showTransactions();
    }

    private void showTransactions() {
        transactionList = loadTransactionsFromDatabase();
        colId.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("transactionId"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("appointmentId"));
        colDate.setCellValueFactory(new PropertyValueFactory<TransactionVM, Date>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("amount"));
        colBalance.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("balance"));
        colCash.setCellValueFactory(new PropertyValueFactory<TransactionVM, Double>("cash"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<TransactionVM, Integer>("discount"));
        tvTransactions.setItems(transactionList);

    }

    private ObservableList<TransactionVM> loadTransactionsFromDatabase() {
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

    //Generating Reports
    public void generateReport(ActionEvent event) {
        ExportToExcel ex = new ExportToExcel(tvTransactions, stackpane);
        ex.run();

    }

    public void filterbyDate() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TransactionVM> filteredItems = new FilteredList<>(transactionList);

        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate minDate = dpSearchFrom.getValue();
                    LocalDate maxDate = dpSearchTo.getValue();

                    // get final values != null
                    final LocalDate finalMin = minDate == null ? LocalDate.MIN : minDate;
                    final LocalDate finalMax = maxDate == null ? LocalDate.MAX : maxDate;

                    // values for openDate need to be in the interval [finalMin, finalMax]
                    return ti -> !finalMin.isAfter(ti.getDate().toLocalDate()) && !finalMax.isBefore(ti.getDate().toLocalDate());
                },
                dpSearchFrom.valueProperty(),
                dpSearchTo.valueProperty()));

        tvTransactions.setItems(filteredItems);

    }


}
