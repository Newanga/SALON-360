package view_models_dashboard;

public class AppointmentVM {
    private int totalAppointmentsToday;
    private int totalAppointments;

    public AppointmentVM() {
    }



    public int getTotalAppointmentsToday() {
        return totalAppointmentsToday;
    }

    public void setTotalAppointmentsToday(int totalAppointmentsToday) {
        this.totalAppointmentsToday = totalAppointmentsToday;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }
}
