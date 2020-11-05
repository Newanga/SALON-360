package DataAccess;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerStateDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public CustomerStateDAO(Connection conn) {
        this.conn = conn;
    }

    public int getCustomerStateIdByName(String name){
        final String sql = "SELECT Id FROM CustomerState WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("id");
            return id;
        } catch (SQLException throwables) {
            return 0;
        }
    }

    public List<String> getAllCustomerStateNames() throws SQLException {
        List<String> CustomerStateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM customerstate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("name");
                CustomerStateNames.add(name);
            }
            return CustomerStateNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return CustomerStateNames;
    }


    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
