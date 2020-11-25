package data_access;

import models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    //Load all customers from database
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        final String sql = "SELECT c.Id,c.FirstName,c.LastName,c.Address,c.ContactNo,c.DOB,c.Email,g.Name as gender,cs.Name as state  FROM customer as c\n" +
                "inner join gender as g\n" +
                "on c.GenderId=g.Id\n" +
                "inner join customerstate as cs\n" +
                "on c.StateId=cs.Id;";


        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();

            while (result.next()) {
                Customer cust = new Customer(result.getInt("Id"), result.getString("firstName"), result.getString("lastName"), result.getDate("dOB"), result.getString("ContactNo"), result.getString("Address"), result.getString("Email"), result.getString("Gender"), result.getString("State"));
                customers.add(cust);
            }
            return customers;
        } catch ( SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }


    //Update an existing customer
    public boolean UpdateCustomer(Customer model) throws SQLException {
        GenderDAO genderDAO = null;
        CustomerStateDAO customerStateDAO = null;

        final String sql="UPDATE Customer SET FirstName=?,LastName=?,DOB=?,Address=?,ContactNo=?,GenderId=?,Email=?,StateId=? WHERE Id=?;";
        try{
            conn.setAutoCommit(false);

            //Get ID from Gender based on Gender type
            genderDAO = new GenderDAO(conn);
            int genderId = genderDAO.getGenderIdByName(model.getGender());

            //Get ID from Customer state based on state
            customerStateDAO = new CustomerStateDAO(conn);
            int customerStateId = customerStateDAO.getCustomerStateIdByName(model.getState());

            statement= conn.prepareStatement(sql);
            statement.setString(1,model.getFirstName());
            statement.setString(2,model.getLastName());
            statement.setDate(3, model.getDob());
            statement.setString(4,model.getAddress());
            statement.setString(5,model.getContactNo());
            statement.setInt(6,genderId);
            statement.setString(7,model.getEmail());
            statement.setInt(8,customerStateId);
            statement.setInt(9,model.getId());
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try{
                conn.setAutoCommit(true);
                if (genderDAO != null)
                    genderDAO.close();
                if (customerStateDAO != null)
                    customerStateDAO.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    //Create a new customer.
    public boolean CreateCustomer(Customer model) throws SQLException {
        GenderDAO genderDAO = null;
        CustomerStateDAO customerStateDAO = null;

        final String sql="INSERT INTO Customer (FirstName,LastName,DOB,Address,ContactNo,GenderId,Email,StateId) Values(?,?,?,?,?,?,?,?);";
        try{
            conn.setAutoCommit(false);

            //Get ID from Gender based on Gender type
            genderDAO = new GenderDAO(conn);
            int genderId = genderDAO.getGenderIdByName(model.getGender());

            //Get ID from Customer state based on state
            customerStateDAO = new CustomerStateDAO(conn);
            int customerStateId = customerStateDAO.getCustomerStateIdByName(model.getState());

            statement= conn.prepareStatement(sql);
            statement.setString(1,model.getFirstName());
            statement.setString(2,model.getLastName());
            statement.setDate(3, (Date) model.getDob());
            statement.setString(4,model.getAddress());
            statement.setString(5,model.getContactNo());
            statement.setInt(6,genderId);
            statement.setString(7,model.getEmail());
            statement.setInt(8,customerStateId);
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try{
                conn.setAutoCommit(true);
                if (genderDAO != null)
                    genderDAO.close();
                if (customerStateDAO != null)
                    customerStateDAO.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }

    }


    public String getCustomerNameById(int id) throws SQLException {
        String custName="";
        final String sql="SELECT CONCAT(FirstName,\" \",LastName) AS name from customer where id=?;";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            result= statement.executeQuery();
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


    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
