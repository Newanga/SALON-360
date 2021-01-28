package helpers.sms;

import data_access.CustomerDAO;
import data_access.DataSource;
import data_access.SMSDAO;
import models.Appointment;
import models.SingleSMS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class AppointmentSMS {

    private static String nonformattedContactNo;

    public static void Confirm(Appointment model, int appointmentId) throws SQLException {
        //Formatting Message
        nonformattedContactNo = AppointmentSMS.GetCustomerPhoneNumber(model.getCustomerId());
        if(nonformattedContactNo==null){
            return;
        }
        String formattedContactNo = TwillioHelper.FormatSenderNumber(nonformattedContactNo);
        String appId=String.valueOf(appointmentId);
        //AAppointment Confirm Message
        String message= "Appointment Id: " + appId + ". Appointment Data: " +model.getAppointmentDate() + ". Appointment Time: " + model.getAppointmentTime() + ". We hope to see you soon.";
        SendSMS(formattedContactNo,message);
    }



    public static void SendSMS(String formattedContactNo, String message){
        TwilioMain twilioMain=new TwilioMain(formattedContactNo,message);
        Boolean isSuccess=twilioMain.SendMessage();
        return;
    }

    public static String GetCustomerPhoneNumber(int id) throws SQLException {
        try{
            DataSource db=new DataSource();
            Connection conn=db.getConnection();
            CustomerDAO customerDAO=new CustomerDAO(conn);
            return customerDAO.getCustomerContactNoById(id);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
