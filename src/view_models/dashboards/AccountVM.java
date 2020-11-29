package view_models.dashboards;

public class AccountVM {
    private int totalAccounts;
    private int activeAccounts;
    private int inactiveAccounts;

    public AccountVM() {
    }



    public int getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public int getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(int activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    public int getInactiveAccounts() {
        return inactiveAccounts;
    }

    public void setInactiveAccounts(int inactiveAccounts) {
        this.inactiveAccounts = inactiveAccounts;
    }
}
