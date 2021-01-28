package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.SMSTemplate;
import models.ServiceCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SMSTemplateDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public SMSTemplateDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean CreateNewSMSTemplate(SMSTemplate model) throws SQLException{
        String sql = "INSERT INTO smstemplate (name, message) VALUES (?, ?);";
        try {
            statement = conn.prepareStatement(sql);
            String name = model.getName();
            String description = model.getMessage();
            statement.setString(1, name);
            statement.setString(2, description);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getSMSTemplateIdByName(String name) throws SQLException {
        final String sql = "SELECT Id FROM smstemplate WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            int id = result.getInt("id");
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public String getSMSTemplateMessagebyName(String name){
        final String sql = "SELECT Message FROM smstemplate WHERE Name=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            result= statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            String message = result.getString("Message");
            return message;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public ObservableList<SMSTemplate> getAllSMSTemplates() throws SQLException {
        ObservableList<SMSTemplate> smsTemplates = FXCollections.observableArrayList();
        final String sql = "SELECT * FROM smstemplate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                SMSTemplate template=new SMSTemplate();
                template.setId(result.getInt("Id"));
                template.setName(result.getString("Name"));
                template.setMessage(result.getString("Message"));
                smsTemplates.add(template);
            }
            return smsTemplates;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return smsTemplates;
    }

    public List<String> getAllSMSTemplateNames() throws SQLException {
        List<String> smsTemplateNames = FXCollections.observableArrayList();
        final String sql = "SELECT Name FROM smstemplate;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                String name =  result.getString("name");
                smsTemplateNames.add(name);
            }
            return smsTemplateNames;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return smsTemplateNames;
    }

    public boolean updateSMSTemplate(SMSTemplate model) throws SQLException {
        final String sql = "UPDATE smstemplate SET Name=?,Message =? WHERE Id=?;";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getName());
            statement.setString(2, model.getMessage());
            statement.setInt(3, model.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
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
