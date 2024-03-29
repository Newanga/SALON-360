package models;

public class ServiceCategory {
    private int id;
    private String name;
    private String description;

    public ServiceCategory() {

    }

    public ServiceCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ServiceCategory(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ServiceCategory(String description) {
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
