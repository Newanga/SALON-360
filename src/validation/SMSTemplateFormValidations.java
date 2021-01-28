package validation;

import models.SMSTemplate;
import models.ServiceCategory;

public class SMSTemplateFormValidations {
    public static Boolean validateEmptyData(SMSTemplate model) {
        if (model.getName().isEmpty() || model.getMessage().isEmpty())
            return false;
        else
            return true;
    }
}
