package view_models.dashboards;

public class MarketingVM {

    private int totalSMSSent;

    private int totalSMSTemplate;

    public MarketingVM() {
    }

    public int getTotalSMSSent() {
        return totalSMSSent;
    }

    public void setTotalSMSSent(int totalSMSSent) {
        this.totalSMSSent = totalSMSSent;
    }

    public int getTotalSMSTemplate() {
        return totalSMSTemplate;
    }

    public void setTotalSMSTemplate(int totalSMSTemplate) {
        this.totalSMSTemplate = totalSMSTemplate;
    }
}
