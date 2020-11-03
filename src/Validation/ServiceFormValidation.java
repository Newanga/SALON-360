package Validation;

import Models.Service;
import Models.ServiceCategory;

public class ServiceForm {

    public static Boolean validateEmptyData(Service model) {
        if (model.getName().isEmpty() || model.getDescription().isEmpty() || model.getPrice() == 0.0d)
            return false;
        else
            return true;
    }

    public static Boolean ValidatePrice(String price) {
        double double_price;
        try {
            double_price = Double.parseDouble(price);
            if(double_price>10000.00)
                return false;
            else
                return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }


}
