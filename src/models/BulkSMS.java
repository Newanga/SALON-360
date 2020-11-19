package models;

import java.util.Date;
import java.util.List;

public class BulkSMS extends SMS {

    private List<Customer> customerlist;

    public BulkSMS(){

    }

    public BulkSMS(List<Customer> customerlist) {
        this.customerlist = customerlist;
    }

    public BulkSMS(int id, SMSTemplate smsTemplate, int modeName, Date sentDate, List<Customer> customerlist) {
        super(id, smsTemplate, modeName, sentDate);
        this.customerlist = customerlist;
    }

    public List<Customer> getCustomerlist() {
        return customerlist;
    }

    public void setCustomerlist(List<Customer> customerlist) {
        this.customerlist = customerlist;
    }
}
