package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;
import models.SingleSMS;
import models.SMSTemplate;
import view_models.dashboards.MarketingVM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class SMSDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public SMSDAO(Connection conn) {
        this.conn = conn;
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public ObservableList<SingleSMS> getAllSentSMS() throws SQLException {
        ObservableList<SingleSMS> singleSmsSentList = FXCollections.observableArrayList();
        final String sql = "SELECT s.Id,st.Name as TemplateName,c.FirstName,c.LastName,s.SentDate\n" +
                "FROM sms as s\n" +
                "inner join smstemplate as st\n" +
                "on s.TemplateId=st.Id\n" +
                "inner join customer as c\n" +
                "on s.CustomerId=c.Id\n" +
                "inner join smsmode as sm\n" +
                "on s.ModeId=sm.id;";
        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                SingleSMS sMS = new SingleSMS();
                sMS.setId(result.getInt("Id"));
                sMS.setSentDate(result.getDate("SentDate"));

                SMSTemplate smsTemplate = new SMSTemplate();
                smsTemplate.setName(result.getString("TemplateName"));
                sMS.setSmsTemplate(smsTemplate);

                Customer customer = new Customer();
                customer.setFirstName(result.getString("FirstName"));
                customer.setLastName(result.getString("LastName"));
                sMS.setCustomer(customer);

                singleSmsSentList.add(sMS);
            }
            return singleSmsSentList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return singleSmsSentList;
    }

    public ObservableList<Customer> getCustomerData() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        final String sql = "SELECT c.Id,c.FirstName,c.LastName,c.ContactNo \n" +
                "FROM customer as c\n" +
                "inner join gender as g\n" +
                "on c.GenderId=g.Id\n" +
                "inner join customerstate as cs\n" +
                "on c.StateId=cs.Id\n" +
                "where cs.Name=\"Active\";";


        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();

            while (result.next()) {
                Customer cust = new Customer();
                cust.setId(result.getInt("id"));
                cust.setFirstName(result.getString("firstName"));
                cust.setContactNo(result.getString("contactNo"));
                cust.setLastName(result.getString("lastName"));
                customers.add(cust);
            }
            return customers;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    public void SaveSingleSMSSentData(SingleSMS model) {
        final String sql = "INSERT into SMS (TemplateId,CustomerId,ModeId,sentDate) Values (?,?,?,?);";

        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1,model.getSmsTemplate().getId());
            statement.setInt(2,model.getCustomer().getId());
            statement.setInt(3,model.getModeId());
            java.util.Date date = Calendar.getInstance().getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setDate(4,sqlDate);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public MarketingVM getDashBoardData() {
        MarketingVM marketingVM= new MarketingVM();

        final String queryTotalSMSSent="select COUNT(SMS.Id) as TotalSMS\n" +
                "from SMS;";

        final String queryTotalSMSTemplates="select COUNT(SMSTemplate.Id) as TotalSMSTemplates\n" +
                                            "from SMSTemplate;";

        try{
            statement = conn.prepareStatement(queryTotalSMSSent);
            result=statement.executeQuery();
            result.absolute(1);
            int totalSMSSent = result.getInt("TotalSMS");
            marketingVM.setTotalSMSSent(totalSMSSent);

            statement = conn.prepareStatement(queryTotalSMSTemplates);
            result=statement.executeQuery();
            result.absolute(1);
            int totalSMSTemplates = result.getInt("TotalSMSTemplates");
            marketingVM.setTotalSMSTemplate(totalSMSTemplates);

            return marketingVM;
        }catch (SQLException ex){
            ex.printStackTrace();
            return marketingVM;
        }
    }
}



