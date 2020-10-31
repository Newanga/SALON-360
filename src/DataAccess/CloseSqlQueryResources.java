package DataAccess;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseSqlQueryResources {

    public static void close(Connection conn,ResultSet set,PreparedStatement statement ) throws SQLException {

        try{
            if(set!=null)
                set.close();
        }
        catch (SQLException ex) {

        }

        try {
            if(conn!=null)
                conn.close();
        }
        catch (SQLException ex){

        }

        try{
            if(statement!=null){
                statement.clearParameters();;
                statement.close();
            }
        }
        catch (SQLException ex) {

        }

    }


}
