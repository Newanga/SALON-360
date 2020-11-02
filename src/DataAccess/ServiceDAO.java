package DataAccess;

import Models.Service;
import Models.ServiceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public ServiceDAO(Connection conn) {
        this.conn = conn;
    }



    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Service> getAllServices() {
        ObservableList<Service> serviceCategories = FXCollections.observableArrayList();
        final String sql ="select s.Id,s.Name,s.Price,s.Description,sc.Name as category ,ss.Name as state from service as s\n" +
                "inner join servicecategory as sc \n" +
                "on s.CategoryId=sc.Id\n" +
                "inner join servicestate as ss\n" +
                "on s.StateId=ss.Id;\n";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Service service = new Service(result.getInt("Id"), result.getString("Name"), result.getString("Description"),result.getDouble("Price"),result.getString("Category"),result.getString("State"));
                serviceCategories.add(service);
            }
            return serviceCategories;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return serviceCategories;
    }
}
