package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public InventoryDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<Inventory> getAllInventory() {
        ObservableList<Inventory> inventory = FXCollections.observableArrayList();
        final String sql = "SELECT i.Id,i.Name,i.Price,i.Quantity,i.Description,i.SpecialNote,ic.Name as Category\n" +
                            "FROM inventory as i\n" +
                            "inner join inventorycategory as ic\n" +
                            "on i.CategoryId=ic.Id;";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Inventory item = new Inventory(result.getInt("Id"), result.getString("Name"), result.getDouble("Price"),result.getInt("Quantity"), result.getString("Description"),result.getString("SpecialNote"),result.getString("Category"));
                inventory.add(item);
            }
            return inventory;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return inventory;
    }

    public Boolean UpdateInventory(Inventory model) throws SQLException {
        DataSource db = new DataSource();
        InventoryCategoryDAO icdao= null;

        final String sql = "UPDATE Inventory SET Name=?,Price=?,Quantity=?,Description=?,SpecialNote=?,CategoryId=? where Id=?;";
        //Implementing a transactions
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            //Get ID from Inventorycatergory based on category name
            icdao = new InventoryCategoryDAO(conn);
            int inventoryCategoryId = icdao.getInventoryCategoryIdByName(model.getCategory());


            //USe the other properties of model and add new record t0 service
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setDouble(2, model.getPrice());
            statement.setInt(3, model.getQuantity());
            statement.setString(4, model.getDescription());
            statement.setString(5, model.getSpecialNote());
            statement.setInt(6, inventoryCategoryId);
            statement.setInt(7, model.getId());
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try{
                conn.setAutoCommit(true);
                if (icdao != null)
                    icdao.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    public Boolean CreateNewInventory(Inventory model) throws SQLException {
        DataSource db = new DataSource();
        InventoryCategoryDAO icdao = null;


        final String sql = "INSERT INTO Inventory (Name,Price,Quantity,Description,SpecialNote,CategoryId) values (?,?,?,?,?,?);";

        //Implementing a transactions
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            //Get ID from inventorycatergory based on category name
            icdao = new InventoryCategoryDAO(conn);
            int inventoryCategoryId = icdao.getInventoryCategoryIdByName(model.getCategory());


            //USe the other properties of model and add new record tt service
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setDouble(2, model.getPrice());
            statement.setInt(3, model.getQuantity());
            statement.setString(4, model.getDescription());
            statement.setString(5, model.getSpecialNote());
            statement.setInt(6, inventoryCategoryId);
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try{
                conn.setAutoCommit(true);
                if (icdao != null)
                    icdao.close();
                ConnectionResources.close(conn);
            }catch (Exception ex){
                ex.printStackTrace();
            }

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
