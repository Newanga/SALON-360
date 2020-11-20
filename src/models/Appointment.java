package models;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

    private int id;

    private Date bookedDate;

    private Date appointmentDate;

    private Time appointmentTime;

    private String state;

    private int customerId;

    private String customerName;

    public Appointment() {
    }

    public Appointment(int id, Date bookedDate, Date appointmentDate, Time appointmentTime, String state, int customerId, String customerName) {
        this.id = id;
        this.bookedDate = bookedDate;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.state = state;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
