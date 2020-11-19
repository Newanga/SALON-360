package helpers.sms;

public class TwillioHelper {

    public static String FormatSenderNumber(String name){
        String formattedString=name.substring(1);
        formattedString="+94"+formattedString  ;
        return formattedString;
    }
}
