package Models;

public class ServiceCategory {
    private int Id;
    private String description;

    public ServiceCategory() {
    }

    public ServiceCategory(String description) {
        this.description = description;
    }

    public ServiceCategory(int id) {
        Id = id;
    }

    public ServiceCategory(int id, String description) {
        Id = id;
        this.description = description;
    }


    public int getId() {
        return Id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
