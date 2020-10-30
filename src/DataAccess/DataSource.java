package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    //Declare the environment (Production or Development)
    private static final String SERVER = "Production";

    //Production DB Access details.
    private static final String PRO_URL = "jdbc:mysql://localHost:3306/saloon";
    private static final String PRO_USERNAME = "root";
    private static final String PRO_PASSWORD = "MyNewPass";

    //Development DB Access details.
    private static final String DEV_URL = "";
    private static final String DEV_USERNAME = "";
    private static final String DEV_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection conn;

        //Create Production connection
        if (SERVER == "Production") {

            try {
                conn = DriverManager.getConnection(PRO_URL, PRO_USERNAME, PRO_PASSWORD);
            } catch (SQLException exmsg) {
                throw exmsg;
            }
        }

        //Create Development connection
        if (SERVER == "Development") {

            try {
                conn = DriverManager.getConnection(DEV_URL, DEV_USERNAME, DEV_PASSWORD);
            } catch (SQLException exmsg) {
                throw exmsg;
            }
        }
        return  conn;
    }
}

