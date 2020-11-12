package models;

import java.sql.Blob;
import java.sql.Date;

public class Employee extends Person {

    private int id;

    private Blob image;

    private String state;

    private String role;

    public Employee(String firstName, String lastName, Date dOB, String address, String contactNo, String email, String gender, int id, Blob image, String state,String role) {
        super(firstName, lastName, dOB, address, contactNo, email, gender);
        this.id = id;
        this.image = image;
        this.state = state;
        this.role=role;
    }

    public Employee() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
