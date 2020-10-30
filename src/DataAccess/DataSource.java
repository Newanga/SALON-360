package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    //Declare the environment (Production or Development)
    private static final String SERVER = "Production";

    //Development DB Access details.
    private static final String PROURL = "";
    private static final String PROUSERNAME = "";
    private static final String PROPASSWORD = "";

    //Production DB Access details.
    private static final String DEVURL = "";
    private static final String DEVUSERNAME = "";
    private static final String DEVPASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection conn;

        //Create Production connection
        if (SERVER == "Production") {

            try {
                conn = DriverManager.getConnection(PROURL, PROUSERNAME, PROPASSWORD);
            } catch (SQLException exmsg) {
                throw exmsg;
            }
        }

        //Create Development connection
        if (SERVER == "Development") {

            try {
                conn = DriverManager.getConnection(DEVURL, DEVUSERNAME, DEVPASSWORD);
            } catch (SQLException exmsg) {
                throw exmsg;
            }
        }
        return  conn;
    }
}

