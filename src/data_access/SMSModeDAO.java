package data_access;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SMSModeDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public SMSModeDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllSMSModeNames() throws SQLException {
        List<String> smsModeNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM smsmode;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("Name");
                smsModeNames.add(name);
            }
            return smsModeNames;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return smsModeNames;
    }

    public int getSMSModeIdByName(String name) throws SQLException {
        final String sql = "SELECT Id FROM smsmode WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("id");
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
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
