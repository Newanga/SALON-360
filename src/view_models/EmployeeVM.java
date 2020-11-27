package view_models;

public class EmployeeVM {

    private int totalEmployees;

    private int activeEmployees;

    private int inactiveEmployees;

    public EmployeeVM() {
    }


    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(int activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public int getInactiveEmployees() {
        return inactiveEmployees;
    }

    public void setInactiveEmployees(int inactiveEmployees) {
        this.inactiveEmployees = inactiveEmployees;
    }
}
