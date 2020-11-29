package main;

public class CurrentUserData {

    private static String role;
    private static String accId;
    private static String empId;
    private static String email;
    private static String fullName;

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        CurrentUserData.role = role;
    }

    public static String getAccId() {
        return accId;
    }

    public static void setAccId(String accId) {
        CurrentUserData.accId = accId;
    }

    public static String getEmpId() {
        return empId;
    }

    public static void setEmpId(String empId) {
        CurrentUserData.empId = empId;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUserData.email = email;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        CurrentUserData.fullName = fullName;
    }
}
