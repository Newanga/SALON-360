package data_access;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeStateDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public EmployeeStateDAO(Connection conn) {
        this.conn = conn;
    }

    public int getEmployeeStateIdByName(String name){
        final String sql = "SELECT Id FROM employeestate WHERE Name=?";
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

    public List<String> getAllEmployeeStateNames() throws SQLException {
        List<String> EmployeeStateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM employeestate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("name");
                EmployeeStateNames.add(name);
            }
            return EmployeeStateNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return EmployeeStateNames;
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
