package helpers.sms;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioMain {


    private static final String ACCOUNT_SID = "<YOUR SID>";
    private static final String AUTH_TOKEN ="<Your Token>";

    private String phoneNo;
    private String messagebody;
    private boolean isSuccess=false;

    public TwilioMain(String phoneNo, String messagebody) {
        this.phoneNo = phoneNo;
        this.messagebody = messagebody;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void SendMessage() {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                    //Format ("<+94><xxxxxxxxx>")
                    new com.twilio.type.PhoneNumber(phoneNo),
                    //From number(Trial account number)
                    new com.twilio.type.PhoneNumber("+19123485025"),messagebody
            ).create();

            String msgSid=message.getSid();

            if (msgSid.contains("SM"))
                isSuccess= true;
            else
                isSuccess= false;

    }

}
