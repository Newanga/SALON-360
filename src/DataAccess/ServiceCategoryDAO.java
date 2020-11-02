package DataAccess;

import Models.ServiceCategory;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public ServiceCategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public Boolean createNewServiceCategory(ServiceCategory model) throws SQLException {
        String sql = "INSERT INTO servicecategory (name, description) VALUES (?, ?);";
        try {
            statement = conn.prepareStatement(sql);
            String name = model.getName();
            String description = model.getDescription();
            statement.setString(1, name);
            statement.setString(2, description);
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    public int getServiceCategoryIdByName(String name) throws SQLException {
        int id = 0;
        final String sql = "SELECT Id FROM servicecategory WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            id = statement.executeUpdate();
            return id;
        } catch (SQLException throwables) {
            return id;
        }
    }

    public ObservableList<ServiceCategory> getAllServiceCategories() throws SQLException {
        ObservableList<ServiceCategory> serviceCategories = FXCollections.observableArrayList();
        final String sql = "SELECT * FROM servicecategory;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                ServiceCategory sc = new ServiceCategory(result.getInt("Id"), result.getString("Name"), result.getString("Description"));
                serviceCategories.add(sc);
            }
            return serviceCategories;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return serviceCategories;
    }

    public List<String> getAllServiceCategoryNames() throws SQLException {
        List<String> serviceCategoryNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM servicecategory;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
               String name =  result.getString("name");
               serviceCategoryNames.add(name);
            }
            return serviceCategoryNames;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return serviceCategoryNames;
    }

    public boolean updateServiceCategory(ServiceCategory model) throws SQLException {
        final String sql = "UPDATE servicecategory SET Name=?,Description =? WHERE Id=?;";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setString(2, model.getDescription());
            statement.setInt(3, model.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
