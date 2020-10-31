package tests;


import DataAccess.DataSource;
import DataAccess.ServiceCategoryDAO;
import Models.ServiceCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceCategorySQLQueryTest {


    @Test
    public  void Test() throws SQLException {
        DataSource db =new DataSource();
        Connection conn=db.getConnection();
        try {
            conn.setAutoCommit(false);
            ServiceCategory s1 = new ServiceCategory("n1","d1");
            ServiceCategory s2 = new ServiceCategory("n2","d2");

            ServiceCategoryDAO dao=new ServiceCategoryDAO(conn);

            dao.createNewServiceCategory(s1);
            dao.createNewServiceCategory(s2);

            assertEquals(1,dao.getServiceCategoryIdByName("n1") );
            assertEquals(2, dao.getServiceCategoryIdByName("n2"));

            conn.setAutoCommit(true);
            conn.rollback();
            dao.close();

        }
        catch (SQLException ex){

        }


    }


}


