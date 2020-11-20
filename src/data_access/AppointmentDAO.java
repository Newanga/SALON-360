package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;
import scala.App;

import java.sql.*;
import java.time.LocalDate;

public class AppointmentDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<Appointment> getAllAppointmentsToday(){
        ObservableList<Appointment> appointmentsTodayList = FXCollections.observableArrayList();

        final String sql="SELECT ap.id,ap.AppointmentDate,ap.AppointmentTime,concat(c.FirstName,\" \",c.LastName) as CustomerName, aps.Name as state\n" +
                "FROM appointment as ap\n" +
                "inner join appointmentstate as aps\n" +
                "on ap.StateId=aps.Id\n" +
                "inner join customer as c\n" +
                "on ap.CustomerId=c.Id\n" +
                "where ap.AppointmentDate=?;";

        try{
            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            result = statement.executeQuery();

            while (result.next()){
                Appointment app=new Appointment();
                app.setId(result.getInt("Id"));
                app.setCustomerName(result.getString("CustomerName"));
                app.setAppointmentDate(result.getDate("AppointmentDate"));
                app.setAppointmentTime(result.getTime("AppointmentTime"));
                app.setState(result.getString("State"));
                appointmentsTodayList.add(app);
            }
            return appointmentsTodayList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsTodayList;

    }

    public ObservableList<Appointment> getAllAppointmentsByDateAndTime(Date searchDate, Time searchTime) {
        ObservableList<Appointment> appointmentsFilteredList = FXCollections.observableArrayList();


        final String sql="SELECT ap.id,ap.AppointmentDate,ap.BookedDate,ap.AppointmentTime,concat(c.FirstName,c.LastName) as CustomerName, aps.Name as state\n" +
                "FROM appointment as ap\n" +
                "inner join appointmentstate as aps\n" +
                "on ap.StateId=aps.Id\n" +
                "inner join customer as c\n" +
                "on ap.CustomerId=c.Id\n" +
                "where ap.AppointmentDate=?\n" +
                "AND ap.AppointmentTime Between SUBTIME(?,\"02:00:00\") AND ADDTIME(?,\"02:00:00\") ;";

        try{
            statement = conn.prepareStatement(sql);
            statement.setDate(1, searchDate);
            statement.setTime(2, searchTime);
            statement.setTime(3, searchTime);
            result = statement.executeQuery();

            while (result.next()){
                Appointment app=new Appointment();
                app.setId(result.getInt("Id"));
                app.setCustomerName(result.getString("CustomerName"));
                app.setAppointmentDate(result.getDate("AppointmentDate"));
                app.setAppointmentTime(result.getTime("AppointmentTime"));
                app.setState(result.getString("State"));
                appointmentsFilteredList.add(app);
            }
            return appointmentsFilteredList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentsFilteredList;
    }

    public ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        final String sql="SELECT ap.id,ap.AppointmentDate,ap.BookedDate,ap.AppointmentTime,concat(c.FirstName,c.LastName) as CustomerName, aps.Name as state\n" +
                "FROM appointment as ap\n" +
                "inner join appointmentstate as aps\n" +
                "on ap.StateId=aps.Id\n" +
                "inner join customer as c\n" +
                "on ap.CustomerId=c.Id;";


        try{
            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            result = statement.executeQuery();

            while (result.next()){
                Appointment app=new Appointment();
                app.setId(result.getInt("Id"));
                app.setCustomerName(result.getString("CustomerName"));
                app.setAppointmentDate(result.getDate("AppointmentDate"));
                app.setAppointmentTime(result.getTime("AppointmentTime"));
                app.setState(result.getString("State"));
                app.setBookedDate(result.getDate("BookedDate"));
                allAppointments.add(app);
            }
            return allAppointments;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allAppointments;

    }

    public Boolean CreateNewAppointment(Appointment model){
        final String sql="INSERT INTO (BookedDate,AppointmentDate,AppointmentTime,StateId,CustomerId) VALUES (?,?,?,?,?);";
        AppointmentStateDAO appointmentStateDAO=new AppointmentStateDAO(conn);
        int stateId=0;
        try{

            //get state id
            stateId=appointmentStateDAO.getAppointmentStateIdByName(model.getState());

            statement=conn.prepareStatement(sql);
            statement.setDate(1,model.getBookedDate());
            statement.setDate(2,model.getAppointmentDate());
            statement.setTime(3,model.getAppointmentTime());
            statement.setInt(4,stateId);
            statement.setInt(5,model.getCustomerId());
            return statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Boolean UpdateAppointment(Appointment model){
        final String sql="UPDATE Appointment Set AppointmentDate=?,AppointmentTime=?,StateId=? where Id=?;";
        AppointmentStateDAO appointmentStateDAO=new AppointmentStateDAO(conn);
        int stateId=0;
        try{

            //get state id
            stateId=appointmentStateDAO.getAppointmentStateIdByName(model.getState());

            statement=conn.prepareStatement(sql);
            statement.setDate(1,model.getAppointmentDate());
            statement.setTime(2,model.getAppointmentTime());
            statement.setInt(3,stateId);
            statement.setInt(4,model.getId());
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
