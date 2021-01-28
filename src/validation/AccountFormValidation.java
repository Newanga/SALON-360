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


}
