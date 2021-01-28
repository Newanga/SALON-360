package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionResources {

    //These method overloads will close all the sql related resources created during a database operation.

    public static void close(Connection conn) throws SQLException {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(PreparedStatement statement) throws SQLException {
        try {
            if (statement != null) {
                statement.clearParameters();
                statement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(ResultSet set, PreparedStatement statement) throws SQLException {
        try {
            if (set != null)
                set.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        try {
            if (statement != null) {
                statement.clearParameters();
                statement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    public static void close(ResultSet set, PreparedStatement statement, Connection conn) throws SQLException {

        try {
            if (set != null)
                set.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        try {
            if (statement != null) {
                statement.clearParameters();
                statement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


}
