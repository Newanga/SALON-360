package DataAccess;

import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceStateDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public ServiceStateDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllServiceStateNames() throws SQLException {
        List<String> serviceStateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM servicestate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("name");
                serviceStateNames.add(name);
            }
            return serviceStateNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return serviceStateNames;
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
