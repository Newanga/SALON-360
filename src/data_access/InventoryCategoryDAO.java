package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.InventoryCategory;
import models.ServiceCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InventoryCategoryDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public InventoryCategoryDAO(Connection conn) {
        this.conn = conn;
    }
    public Boolean createNewInventoryCategory( InventoryCategory model) throws SQLException {
        String sql = "INSERT INTO inventorycategory (name, description) VALUES (?, ?);";
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

    public int getInventoryCategoryIdByName(String name) throws SQLException {
        final String sql = "SELECT Id FROM inventorycategory WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("id");
            return id;
        } catch (SQLException throwables) {
            return 0;
        }
    }

    public ObservableList<InventoryCategory> getAllInventoryCategories() throws SQLException {
        ObservableList<InventoryCategory> inventoryCategories = FXCollections.observableArrayList();
        final String sql = "SELECT * FROM inventorycategory;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                InventoryCategory sc = new InventoryCategory(result.getInt("Id"), result.getString("Name"), result.getString("Description"));
                inventoryCategories.add(sc);
            }
            return inventoryCategories;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return inventoryCategories;
    }

    public List<String> getAllInventoryCategoryNames() throws SQLException {
        List<String> serviceCategoryNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM inventorycategory;";
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

    public boolean updateInventoryCategory(InventoryCategory model) throws SQLException {
        final String sql = "UPDATE inventorycategory SET Name=?,Description =? WHERE Id=?;";
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
