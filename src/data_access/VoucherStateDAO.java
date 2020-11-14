package data_access;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VoucherStateDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public VoucherStateDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllVoucherStateNames() throws SQLException {
        List<String> voucherStateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM voucherstate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("Name");
                voucherStateNames.add(name);
            }
            return voucherStateNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return voucherStateNames;
    }

    public int getVoucherStateIdByName(String name) throws SQLException {
        final String sql = "SELECT Id FROM voucherstate WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("Id");
            return id;
        } catch (SQLException throwables) {
            return 0;
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
