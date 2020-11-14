package validation;

import models.Voucher;

public class VoucherFormValidation {
    public static Boolean ValidateAmount(String amount) {
        int int_Amount;
        try {
            int_Amount = Integer.parseInt(amount);
            if(int_Amount>1000 || int_Amount<100)
                return false;
            else
                return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }

//    public static boolean validateEmptyData(Voucher voucherModel) {
//        if(voucherModel.getAmount())
//    }
}
