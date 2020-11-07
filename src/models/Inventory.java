package models;

public class Inventory {

    private int id;

    private String name;

    private double price;

    private int quantity;
    private String description;

    private String specialNote;

    private String category;

    public Inventory() {
    }

    public Inventory(int id, String name,double price, int quantity, String description, String specialNote, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.specialNote = specialNote;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
