package tests;

import DataAccess.DataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBConnectionTest {

    @Test
    public void devConnectionTest() throws SQLException {
        Connection connection = DataSource.getConnection();
        assertEquals(connection != null, true);
    }
}
