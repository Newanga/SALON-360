package models;

import java.util.Date;

public class SingleSMS extends SMS {
    private Customer customer;

    public SingleSMS(){

    }

    public SingleSMS(int id, SMSTemplate smsTemplate, int modeName, Date sentDate, Customer customer) {
        super(id, smsTemplate, modeName, sentDate);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}





