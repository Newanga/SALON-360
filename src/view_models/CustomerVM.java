package view_models;

public class CustomerVM {

    private int totalCustomers;

    private int maleCustomers;

    private int femaleCustomers;

    private int InactiveCustomers;

    public CustomerVM() {
    }



    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getMaleCustomers() {
        return maleCustomers;
    }

    public void setMaleCustomers(int maleCustomers) {
        this.maleCustomers = maleCustomers;
    }

    public int getFemaleCustomers() {
        return femaleCustomers;
    }

    public void setFemaleCustomers(int femaleCustomers) {
        this.femaleCustomers = femaleCustomers;
    }

    public int getInactiveCustomers() {
        return InactiveCustomers;
    }

    public void setInactiveCustomers(int inactiveCustomers) {
        InactiveCustomers = inactiveCustomers;
    }
}
