package Validation;

import Models.ServiceCategory;

public class ServiceCategoryFormValidation {

    public static Boolean validate(ServiceCategory model) {
        if (model.getName().isEmpty() || model.getDescription().isEmpty())
            return false;
        else
            return true;
    }
}
