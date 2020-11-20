package data_access;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppointmentStateDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public AppointmentStateDAO(Connection conn) {
        this.conn = conn;
    }

    public int getAppointmentStateIdByName(String name) {
        final String sql = "SELECT Id FROM AppointmentState WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result = statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("id");
            return id;
        } catch (SQLException throwables) {
            return 0;
        }
    }

    public List<String> getAllAppointmentStateNames() throws SQLException {
        List<String> AppointmentStateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM Appointmentstate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("Name");
                AppointmentStateNames.add(name);
            }
            return AppointmentStateNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return AppointmentStateNames;
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
