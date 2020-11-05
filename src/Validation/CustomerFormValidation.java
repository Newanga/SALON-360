package Validation;

import Models.Customer;
import Models.Service;

public class CustomerFormValidation {

    public static Boolean validateEmptyData(Customer model) {
        if (
                model.getFirstName().isEmpty() ||
                        model.getLastName().isEmpty() ||
                        model.getEmail().isEmpty() ||
                        model.getAddress().isEmpty() ||
                        model.getContactNo().isEmpty() ||
                        model.getDob().toString() == null
        )
            return true;
        else
            return false;
    }

    public static boolean validateContactNo(String contactNo) {
        boolean valid;

        try {
            Integer.parseInt(contactNo);
            valid = true;
        } catch (NumberFormatException e) {
            valid = false;
        }

        if (contactNo.length() == 10)
            valid = true;
        else
            valid = false;

        return valid;

    }

    public static boolean ValidateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
