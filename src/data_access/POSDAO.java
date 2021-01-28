package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Service;
import models.Transaction;
import models.TransactionItem;

import java.sql.*;

public class POSDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public POSDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<Service> getAllServices() throws SQLException {
        ObservableList<Service> serviceCategories = FXCollections.observableArrayList();
        final String sql = "select s.Id,s.Name,s.Price,sc.Name as category  from service as s\n" +
                "inner join servicecategory as sc \n" +
                "on s.CategoryId=sc.Id\n" +
                "inner join servicestate as ss\n" +
                "on s.StateId=ss.Id\n" +
                "where ss.Name=\"Available\";";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Service service = new Service();
                service.setId(result.getInt("Id"));
                service.setName(result.getString("Name"));
                service.setPrice(result.getDouble("Price"));
                service.setCategory(result.getString("category"));
                serviceCategories.add(service);
            }
            return serviceCategories;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serviceCategories;
    }

    public String CheckAppointmentValidity(int id) throws SQLException {
        String custName =null;
        final String sql = "select CONCAT(FirstName,\" \",LastName) AS name\n" +
                "from appointment as a\n" +
                "inner join customer as c\n" +
                "on a.CustomerId=c.Id\n" +
                "inner join appointmentstate as appstate\n" +
                "on a.StateId=appstate.Id\n" +
                "where a.Id=? AND appstate.Name=\"Confirmed\";";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            result = statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            custName = result.getString("name");
            return custName;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int CheckVoucherValidity(int id) throws SQLException {
        int amount = 0;
        final String sql = "SELECT Amount \n" +
                "FROM voucher \n" +
                "inner join voucherstate\n" +
                "on voucher.StateId=voucherstate.Id\n" +
                "where voucher.Id=? AND  voucherstate.Name=\"Valid\";";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            result = statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            amount = result.getInt("Amount");
            return amount;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;

    }

    public Boolean CreateNewTransaction(Transaction model) throws SQLException {

        final String sql="Insert into transaction (AppointmentId,Date,Time,Cash,Balance) values (?,?,?,?,?);";

        try{
            conn.setAutoCommit(false);

            //Insert Transaction
            statement =conn.prepareStatement(sql);
            statement.setInt(1,model.getAppoinmentId());
            statement.setDate(2,model.getDate());
            statement.setTime(3,model.getTime());
            statement.setDouble(4,model.getCash());
            statement.setDouble(5,model.getBalance());
            statement.executeUpdate();

            //Get Transaction Insert ID from DB
            final  String query="SELECT MAX(ID) as id from Transaction;";
            statement =conn.prepareStatement(query);
            result = statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int transactionId = result.getInt("id");

            //Insert Transaction Items
            final  String queryInsertTransaction="INSERT INTO transactionitem (TransactionId,ServiceId,Price) VALUES (?,?,?);";
            for (int transactionItemIndex = 0; transactionItemIndex < model.getTransactionItems().size(); transactionItemIndex++) {
                TransactionItem transactionItem = new TransactionItem();
                transactionItem=model.getTransactionItems().get(transactionItemIndex);
                statement =conn.prepareStatement(queryInsertTransaction);
                statement.setInt(1,transactionId);
                statement.setInt(2,transactionItem.getService().getId());
                statement.setDouble(3,transactionItem.getService().getPrice());
                statement.execute();
            }

            //Change Appointment State to complete.
            final String queryUpdateAppointmentState="UPDATE appointment set StateId=2 where Id=?";
            statement=conn.prepareStatement(queryUpdateAppointmentState);
            statement.setInt(1,model.getAppoinmentId());
            statement.execute();


            //If A voucher code is used in the transaction, Then update voucher state and transaction id
            if(model.getVoucherId()!=0){
                final String queryUpdateVoucherState="UPDATE voucher set TransactionId=?,StateId=2 where Id=?";
                statement=conn.prepareStatement(queryUpdateVoucherState);
                statement.setInt(1,transactionId);
                statement.setInt(2,model.getVoucherId());
                statement.execute();
            }
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        }finally {
            conn.setAutoCommit(true);
            if(conn!=null){
                ConnectionResources.close(conn);
            }
        }

    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
