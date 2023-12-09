import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class AdminLogIn 
{
Scanner sc=null;
void LogIn()
{
sc=new Scanner(System.in);
String username,password;
System.out.println("enter username");
username=sc.next();
System.out.println("enter password");
password=sc.next();
try
{
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
    String sql="select * from admin";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while(rs.next())
    {
    if(username.equals(rs.getString(2)) && password.equals(rs.getString(3)))
    {
    	System.out.println("Welcome!! Admin");
    	process();
    }
    else
    {
    
    }
    }
}
catch(Exception e)
{
	e.printStackTrace();
}
}
void process()
{
String option="y";
int choice;
  while(option.equalsIgnoreCase("y"))
  {
  System.out.println("1.View Customer Application");
  System.out.println("2.View customer History");
  System.out.println("3.LogOut");
  System.out.println("select your option");
  choice =sc.nextInt();
  switch(choice)
  {
  case 1: customer_application(); 
  break;
  case 2: 
  break;
  case 3: System.out.println("Admin LogOut");
  }
  sc=new Scanner(System.in);
  System.out.println("Do You Want to Continue Press Y or For Exit Press E");
  option=sc.next();
  }
}
void customer_application()
{
int cid;
String status;
try
{
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
    String sql="select * from customer";
    System.out.println("list of customer application");
    Statement st= con.createStatement();
    ResultSet rs= st.executeQuery(sql);
    System.out.println("cid\tcname\tcuser\tcemail\t\tcmobile\t\tstatus\t");
    while(rs.next())
    {
    	System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getString(7));
    }
    System.out.println("Hello Admin ... Please Select Any Customer Approve");
    System.out.println("Enter Customer Id");
    cid=sc.nextInt();
    String sql1="select * from customer where cid='"+cid+"'";
    Statement st1= con.createStatement();
    ResultSet rs1= st1.executeQuery(sql1);
    if(rs1.next())
    {
    	String cemail= rs1.getString(5);
    	String cname= rs1.getString(2);
    	String uname= rs1.getString(3);
    	System.out.println("Coustmer Id is Found Success");
        System.out.println("For Approve Press y or Reject Press R");
        status=sc.next();
        if(status.equalsIgnoreCase("y"))
        {
        	status="yes";
        }
        else
        {
        	status="no";
        }
        String sql2="update customer set status='"+status+"' where cid='"+cid+"'";
        PreparedStatement ps= con.prepareStatement(sql2);
        int i=ps.executeUpdate();
        if(i==1)
        {
        
        	System.out.println("Customer Application is Approved");
        	//API twilio integration for sending sms to customer future enhancement
        	  String host="smtp.gmail.com";   // Types of gmail sending hostname []
			  final String user="akshaymunna006@gmail.com";//change accordingly  
			  final String password="kmxx dqab ngfp siol";//change accordingly  
			    
			  String to=cemail;//change accordingly  
			  
			   //Get the session object  
			   Properties props = new Properties();  
			   props.put("mail.smtp.host",host); 
			   props.put("mail.smtp.auth", "true");  
			   props.put("mail.smtp.starttls.enable", "true");
			   Session session = Session.getDefaultInstance(props,  
			    new javax.mail.Authenticator() {  
			      protected PasswordAuthentication getPasswordAuthentication() {  
			    return new PasswordAuthentication(user,password);  
			      }  
			    });  
			  
			   //Compose the message  
			    try {  
			     MimeMessage message = new MimeMessage(session);  
			     message.setFrom(new InternetAddress(user));  
			     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			     message.setSubject("SBI-Online Banking System -");  
			     message.setText("Dear,\n "+cname+"Welcome to SBI Banking System\n You're Application Is Verified Successfully\n Please LogIn with Your username-"+uname+"and You're password Credientials Submit Your Aadhar card, Pancard, Ration card For Furthrt Verification. \n\n\n\n\n\n\n Thanks\n SBI-Banking System"); 
			    //send the message  
			     Transport.send(message);  
			  
			     System.out.println("message sent successfully...");  
			   
			     } 
			    catch (MessagingException e)
			    {
			    	 e.printStackTrace();
			    	}
        	 String sql3="select * from customer where cid='"+cid+"'"; //Message API Twilio
             Statement st2= con.createStatement();
             ResultSet rs2= st2.executeQuery(sql3);
             if(rs2.next())
             {
            	
             	String mobile=rs2.getString(6);
             	System.out.println(mobile);
             	String sms="your account verified success";
             	MeaageApi ma=new MeaageApi(mobile,sms);//class name object =new class name
             	ma.SendSms();//method calling
             }
        else
        {
        	System.out.println("Customer Application is Not Approved or Rejected");
        }
        }
    }
    else
    {
    	System.out.println("Coustmer Id is Not Found ");
    }
}
catch(Exception e)
{
	e.printStackTrace();
}
}
}

