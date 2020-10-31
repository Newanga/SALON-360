package DataAccess;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseConnection {

    //This method will close all the sql related resources created during a database operation.
    public static void close(@Nullable ResultSet set, @Nullable PreparedStatement statement,@Nullable Connection conn ) throws SQLException {

        try{
            if(set!=null)
                set.close();
        }
        catch (SQLException ex) {

        }


        try{
            if(statement!=null){
                statement.clearParameters();;
                statement.close();
            }
        }
        catch (SQLException ex) {

        }

        try {
            if(conn!=null)
                conn.close();
        }
        catch (SQLException ex){

        }


    }


}
