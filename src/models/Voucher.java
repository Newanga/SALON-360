package models;

import java.sql.Date;

public class Voucher {
    private int id;

    private int amount;

    private Date dateAdded;

    private String specialNotes;

    private String state;

    public Voucher() {
    }

    public Voucher(int id, int amount, Date dateAdded, String specialNotes, String state) {
        this.id = id;
        this.amount = amount;
        this.dateAdded = dateAdded;
        this.specialNotes = specialNotes;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
