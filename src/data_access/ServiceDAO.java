package data_access;

import models.Service;
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


    public ObservableList<Service> getAllServices() {
        ObservableList<Service> serviceCategories = FXCollections.observableArrayList();
        final String sql = "select s.Id,s.Name,s.Price,s.Description,sc.Name as category ,ss.Name as state from service as s\n" +
                "inner join servicecategory as sc \n" +
                "on s.CategoryId=sc.Id\n" +
                "inner join servicestate as ss\n" +
                "on s.StateId=ss.Id;\n";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Service service = new Service(result.getInt("Id"), result.getString("Name"), result.getString("Description"), result.getDouble("Price"), result.getString("Category"), result.getString("State"));
                serviceCategories.add(service);
            }
            return serviceCategories;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return serviceCategories;
    }


    public Boolean UpdateService(Service model) throws SQLException {
        ServiceCategoryDAO scdao = null;
        ServiceStateDAO ssdao = null;

        final String sql = "UPDATE service SET Name=?,Price=?,Description=?,CategoryId=?,StateId=? where Id=?;";
        //Implementing a transactions
        try {
            conn.setAutoCommit(false);

            //Get ID from Servicecatergory based on category name
            scdao = new ServiceCategoryDAO(conn);
            int serviceCategoryId = scdao.getServiceCategoryIdByName(model.getCategory());

            //Get ID from servicestate based on state name
            ssdao = new ServiceStateDAO(conn);
            int serviceStateId = ssdao.getServiceStateIdByName(model.getState());

            //USe the other properties of model and add new record t0 service
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setDouble(2, model.getPrice());
            statement.setString(3, model.getDescription());
            statement.setInt(4, serviceCategoryId);
            statement.setInt(5, serviceStateId);
            statement.setInt(6, model.getId());
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
                if (scdao != null)
                    scdao.close();
                if (ssdao != null)
                    ssdao.close();
                if(conn!=null)
                    ConnectionResources.close(conn);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }


    public Boolean CreateNewService(Service model) throws SQLException {
        ServiceCategoryDAO scdao = null;
        ServiceStateDAO ssdao = null;

        final String sql = "INSERT INTO service (Name,Price,Description,CategoryId,StateId) values (?,?,?,?,?);";

        //Implementing a transactions
        try {
            conn.setAutoCommit(false);

            //Get ID from Servicecatergory based on category name
            scdao = new ServiceCategoryDAO(conn);
            int serviceCategoryId = scdao.getServiceCategoryIdByName(model.getCategory());

            //Get ID from servicestate based on state name
            ssdao = new ServiceStateDAO(conn);
            int serviceStateId = ssdao.getServiceStateIdByName(model.getState());

            //USe the other properties of model and add new record tt service
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setDouble(2, model.getPrice());
            statement.setString(3, model.getDescription());
            statement.setInt(4, serviceCategoryId);
            statement.setInt(5, serviceStateId);
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
                if (scdao != null)
                    scdao.close();
                if (ssdao != null)
                    ssdao.close();
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
