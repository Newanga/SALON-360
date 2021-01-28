package data_access;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenderDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public GenderDAO(Connection conn) {
        this.conn = conn;
    }

    public int getGenderIdByName(String name)  throws SQLException{
        final String sql = "SELECT Id FROM Gender WHERE Name=?";
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

    public List<String> getAllGenderSNames() throws SQLException {
        List<String> genders = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM Gender;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("name");
                genders.add(name);
            }
            return genders;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return genders;
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
