package helpers.sms;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioMain {

    //These should be non-test credentials
    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";

    private String phoneNo;
    private String messagebody;
    private boolean isSuccess = false;

    public TwilioMain(String phoneNo, String messagebody) {
        this.phoneNo = phoneNo;
        this.messagebody = messagebody;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Boolean SendMessage() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                //Format ("<+94><xxxxxxxxx>")
                new com.twilio.type.PhoneNumber(phoneNo),
                //From number(Trial account number)
                new com.twilio.type.PhoneNumber(""), messagebody
        ).create();

        String msgSid = message.getSid();

        if (msgSid.contains("SM"))
            isSuccess = true;
        else
            isSuccess = false;

        return isSuccess;
    }

}
