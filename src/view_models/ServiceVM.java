package view_models;

public class ServiceVM {

    private int totalService;

    private int serviceCategory;

    private int availableServices;

    private int nonAvailableServices;

    private int discontinuedServices;

    public ServiceVM() {
    }

    public int getTotalService() {
        return totalService;
    }

    public void setTotalService(int totalService) {
        this.totalService = totalService;
    }

    public int getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(int serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public int getAvailableServices() {
        return availableServices;
    }

    public void setAvailableServices(int availableServices) {
        this.availableServices = availableServices;
    }

    public int getNonAvailableServices() {
        return nonAvailableServices;
    }

    public void setNonAvailableServices(int nonAvailableServices) {
        this.nonAvailableServices = nonAvailableServices;
    }

    public int getDiscontinuedServices() {
        return discontinuedServices;
    }

    public void setDiscontinuedServices(int discontinuedServices) {
        this.discontinuedServices = discontinuedServices;
    }
}
