package models;

import java.sql.Date;

public abstract class Person {

    private String firstName;

    private  String lastName;

    private Date dob;

    private String address;

    private String contactNo;

    private String gender;

    private String email;

    public Person(String firstName, String lastName, Date dOB, String address, String contactNo, String email,String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dOB;
        this.address = address;
        this.contactNo = contactNo;
        this.gender = gender;
        this.email = email;
    }

    public Person() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
