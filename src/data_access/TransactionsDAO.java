package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Employee;
import view_models.tables.TransactionVM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionsDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;


    public TransactionsDAO(Connection conn) {
        this.conn = conn;
    }



    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<TransactionVM> getAllTransactions() {
        ObservableList<TransactionVM> transactionVM = FXCollections.observableArrayList();

        final String sql = "SELECT t.id as transactionId, t.AppointmentId as appointmentId ,t.Date,t.Cash,t.Balance, COALESCE( v.amount ,0)as discount, (t.Cash-t.Balance) + COALESCE( v.amount ,0)  as amount\n" +
                "FROM transaction as t\n" +
                "left join voucher as v\n" +
                "on t.id=v.TransactionId;";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                TransactionVM transaction = new TransactionVM();
                transaction.setAmount(result.getDouble("amount"));
                transaction.setAppointmentId(result.getInt("appointmentId"));
                transaction.setBalance(result.getDouble("balance"));
                transaction.setCash(result.getDouble("cash"));
                transaction.setDiscount(result.getInt("discount"));
                transaction.setTransactionId(result.getInt("transactionId"));
                transaction.setDate(result.getDate("date"));
                transactionVM.add(transaction);
            }
            return transactionVM;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return transactionVM;
    }
}
