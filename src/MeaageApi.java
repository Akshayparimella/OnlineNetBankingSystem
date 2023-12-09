import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.math.BigDecimal;
       
public class MeaageApi{
		  // Find your Account Sid and Token at twilio.com/console
			 String mobile,sms;
		     public static final String ACCOUNT_SID = "AC53c1c589c4b6708a4cb377f09c363937";
		     public static final String AUTH_TOKEN = "fea1d8a87e93f11db30e91dbcbe6ea47";
        	 public MeaageApi(String mobile,String sms)
        	  {
        		 this.mobile=mobile;
        		 this.sms=sms;
        	  }
        	 void SendSms()
        	 {
		    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		     Message message = Message.creator(
		      new com.twilio.type.PhoneNumber(mobile),
		      new com.twilio.type.PhoneNumber("+16162174029"),
		     sms)
		    .create();
		    System.out.println(message.getSid());
		  }
		}
