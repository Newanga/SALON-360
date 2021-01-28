package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    //Declare the environment (Production or Development)
    private static final String SERVER = "Pro";

    //Development DB Access details.
    private static final String DEV_URL = "";
    private static final String DEV_USERNAME = "";
    private static final String DEV_PASSWORD = "";

    //Production DB Access details.
    private static final String PRO_URL = "";
    private static final String PRO_USERNAME = "";
    private static final String PRO_PASSWORD = "";

    public Connection getConnection() throws SQLException {
        Connection conn = null;

        //Create Production connection
        if (SERVER == "Pro") {
            try {
                conn = DriverManager.getConnection(PRO_URL, PRO_USERNAME, PRO_PASSWORD);
                return conn;
            } catch (SQLException ex) {
                throw new SQLException("Failed to establish Database Connection");
            }
        }

        //Create Development connection
        if (SERVER == "Dev") {
            try {
                conn = DriverManager.getConnection(DEV_URL, DEV_USERNAME, DEV_PASSWORD);
                return conn;
            } catch (SQLException exmsg) {
                throw new SQLException("Failed to establish Database Connection");
            }
        }
        return conn;
    }


}

