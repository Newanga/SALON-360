package view_models_dashboard;

public class InventoryVM {


    private int totalInventoryCategory;

    private int totalNoOfItems;

    public InventoryVM() {
    }
    public int getTotalInventoryCategory() {
        return totalInventoryCategory;
    }

    public void setTotalInventoryCategory(int totalInventoryCategory) {
        this.totalInventoryCategory = totalInventoryCategory;
    }

    public int getTotalNoOfItems() {
        return totalNoOfItems;
    }

    public void setTotalNoOfItems(int totalNoOfItems) {
        this.totalNoOfItems = totalNoOfItems;
    }
}
