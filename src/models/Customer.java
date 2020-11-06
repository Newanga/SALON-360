package models;

import java.sql.Date;

public class Customer extends Person {

    private int id;

    private String state;

    public Customer(int id, String firstName, String lastName, Date dOB, String address, String contactNo , String email, String gender, String state) {
        super(firstName, lastName,  dOB, contactNo, address, email, gender);
        this.id=id;
        this.state=state;

    }

    public Customer() {
        super();
    }


    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }
}
