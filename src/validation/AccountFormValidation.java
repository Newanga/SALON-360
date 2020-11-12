package validation;

import models.Account;
import models.Employee;

public class AccountFormValidation {
    public static boolean validateEmptyData(Account model) {
        if (
                model.getUsername().isEmpty() ||
                        model.getPassword().isEmpty()
        )
            return true;
        else
            return false;
    }

    public static boolean validateUsername(String Username) {
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        return Username.matches(regex);

    }
    public static boolean validatePassword(String Password) {
        String regex = "^(?i)(?=.*[a-z])(?=.*[0-9])[a-z0-9#.!@$*&_]{5,12}$";
        return Password.matches(regex);
    }



}
