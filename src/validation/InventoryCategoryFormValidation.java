package validation;

import models.InventoryCategory;
import models.ServiceCategory;

public class InventoryCategoryFormValidation {

    public static Boolean validateEmptyData(InventoryCategory model) {
        if (model.getName().isEmpty() || model.getDescription().isEmpty())
            return false;
        else
            return true;
    }
}
