package models;

public class TransactionItem {

    private int id;
    private Service service;

    public TransactionItem() {
    }

    public TransactionItem(int id, Service service, double price) {
        this.id = id;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


}
