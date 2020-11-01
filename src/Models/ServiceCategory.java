package Models;

public class ServiceCategory {
    private int Id;
    private String Name;
    private String Description;

    public ServiceCategory() {

    }

    public ServiceCategory(String name, String description) {
        this.Name = name;
        this.Description = description;
    }

    public ServiceCategory(int id, String name, String description) {
        this.Id = id;
        this.Name = name;
        this.Description = description;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}
