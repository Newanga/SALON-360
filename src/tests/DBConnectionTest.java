package tests;

import data_access.DataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBConnectionTest {

    @Test
    public void devConnectionTest() throws SQLException {
        DataSource db=new DataSource();
        Connection connection = db.getConnection();
        assertEquals(connection != null, true);
    }
}
