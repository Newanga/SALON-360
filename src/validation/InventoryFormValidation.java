package validation;

import models.Inventory;
import models.Service;

public class InventoryFormValidation {

    public static Boolean validateEmptyData(Inventory model) {
        if (model.getName().isEmpty() || model.getDescription().isEmpty() || model.getQuantity()<1 || model.getPrice() == 0.0d )
            return true;
        else
            return false;
    }
    public static Boolean ValidateQuantity(String price) {
        int int_Quantity;
        try {
            int_Quantity = Integer.parseInt(price);
            if(int_Quantity>100)
                return false;
            else
                return true;
        } catch(NumberFormatException ex) {
            return false;
        }
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
