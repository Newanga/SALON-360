package models;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Transaction {

    private int id;
    private List<TransactionItem> transactionItems;
    private int appoinmentId;
    private int vocuherId;
    private Date date;
    private Time time;
    private double cash;
    private double balance;

    public Transaction() {
    }

    public Transaction(int id, List<TransactionItem> transactionItems, int appoinmentId, int vocuherId, Date date, Time time, double cash, double balance) {
        this.id = id;
        this.transactionItems = transactionItems;
        this.appoinmentId = appoinmentId;
        this.vocuherId = vocuherId;
        this.date = date;
        this.time = time;
        this.cash = cash;
        this.balance = balance;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getVocuherId() {
        return vocuherId;
    }

    public void setVocuherId(int vocuherId) {
        this.vocuherId = vocuherId;
    }

    public int getAppoinmentId() {
        return appoinmentId;
    }

    public void setAppoinmentId(int appoinmentId) {
        this.appoinmentId = appoinmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
