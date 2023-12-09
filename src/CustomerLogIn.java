import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class CustomerLogIn {

	Scanner din;
	int cusid;
	void LogType()
	{
		int Choice;
		System.out.println("Select Your Option");
		System.out.println("Press 1.For Login");
		System.out.println("Press 2.For Register");
		din = new Scanner(System.in);
		Choice= din.nextInt();
		switch(Choice)
		{
		case 1:Login();
			break;
		case 2:Register();
			break;
		}
	}
	void Login()
	{
		int customer_id;
		String accountnumber;
		Double balance;
		String dateandtime;
		String status="Yes";
		int account_status=0;
		String username,password;
		int vid;
		System.out.println("Login Here");
		try
		{
			System.out.println("Enter Your UserName");
			username = din.next();
			System.out.println("Enter Your Password");
			password = din.next();
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
			String sql="select * from customer where cuser='"+username+"' and cpass='"+password+"' and status='"+status+"'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next())
			{
				cusid = rs.getInt(1);
				System.out.println("Hello Customer .. Welcome To SBI Banking System");
				String sql2="select c1.cid,c1.cname,c1.status,c2.status,c3.account from customer c1 inner join customer_doc c2 on c1.cid=c2.cid inner join customer_account c3 on c2.cid=c3.cid and c2.status='Approved' and c2.cid='"+cusid+"'";
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery(sql2);
				if(rs1.next())
				{
					System.out.println("Goto Dashboard");
					dashboard(); // Calling Dashboard Menu
				}
				else
				{
					vid = verification(); // verification method calling
					String sql1="select * from customer_doc where cid='"+cusid+"'";
					Statement st3 = con.createStatement();
					ResultSet rs3 = st3.executeQuery(sql1);
					while(rs3.next())
					{
						if(rs3.getString(6).equals("Approved"))
						{
							account_status=1;
						}
					}
					if(account_status==1)
					{
						System.out.println("Hello Customer .. Documents verified Success");
						System.out.println("Create your Account");
						accountCreation(); // Calling Account Creation Method
					}
					else
					{
						System.out.println("Hello Customer .. Sorry !! Document Verification Failed");
						System.out.println("Not Applicable to Create Account");
					}
				}
			}
			else
			{
				System.out.println("Account is Invalid Or Not Verified Or Rejected");
			}
	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	void Register()
	{
		String cname,cuser,cpass,cemail,cmobile;
		System.out.println("Register Here");
		try {
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
			System.out.println("Enter Customer Name");
			cname = din.next();
			System.out.println("Enter Customer UserName");
			cuser = din.next();
			System.out.println("Enter Customer Password");
			cpass = din.next();
			System.out.println("Enter Customer Email");
			cemail = din.next();
			System.out.println("Enter Customer Mobile");
			cmobile = din.next();
			String sql="insert into customer(cname,cuser,cpass,cemail,cmobile)values(?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, cname);
			ps.setString(2, cuser);
			ps.setString(3, cpass);
			ps.setString(4, cemail);
			ps.setString(5, cmobile);
			int i = ps.executeUpdate();
			if(i==1)
			{
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
				     message.setText("Dear,\n "+cname+" You're login Application request sent to Branch Manager\n Please Wait for the Confirmation Mail\n\n\n\n\n Thanks\n SBI-OnlineBanking System ");  
				       
				    //send the message  
				     Transport.send(message);  
				  
				     System.out.println("message sent successfully...");  
				   
				     } 
				    catch (MessagingException e)
				    {
				    	 e.printStackTrace();
				    	
				    }
				System.out.println("Application Successfully Sent to Branch Manager");
				System.out.println("Please Wait for the Verification");
				System.out.println("Try Login Later");
				System.exit(1);
			}
			else
			{
				System.out.println("Application Failed To sent");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	int verification()
	{
		String aadharcard,pancard,rationcard,status;
		int verified=0;
		Scanner din = new Scanner(System.in);
		System.out.println("Press Y For Document submitted or Press N for Not-Submitted");
		System.out.println("Aadhar Card is Submitted-?");
		aadharcard=din.next();  // y
		System.out.println("Pan Card is Submitted-?");
		pancard=din.next(); // y
		System.out.println("Ration Card is Submitted-?");
		rationcard = din.next();  // n
		if(aadharcard.equalsIgnoreCase("y"))
		{
			verified++;
		}
		if(pancard.equalsIgnoreCase("y"))
		{
			verified++;
		}
		if(rationcard.equalsIgnoreCase("y"))
		{
			verified++;
		}
		if(verified>=2)
		{
			status="Approved";
		}
		else
		{
			status="Rejected";
		}
		//System.out.println(status);
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
			String sql="insert into customer_doc values(?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cusid);
			ps.setString(2, aadharcard);
			ps.setString(3, pancard);
			ps.setString(4, rationcard);
			ps.setInt(5, verified);
			ps.setString(6, status);
			int i=ps.executeUpdate();
			if(i>0)
			{
				System.out.println("Documents Saved");
			}
			else
			{
				System.out.println("Documents Not Saved");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}
	void accountCreation()
	{
		String accounttype=null;
		double balance;
		String bank="SBI";
		int count=100;
		int value=0;
		try
		{
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
			String sql="select count(*) from customer_account";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next())
			{
				//System.out.println(rs.getInt(1));
				value = rs.getInt(1);
			}
			count = count + value;
			String cnumber = bank + count;
			System.out.println(cnumber);
			Scanner din = new Scanner(System.in);
			System.out.println("Press 1.For Savings Account\nPress 2.For Current Account");
			int choice = din.nextInt();
			if(choice==1)
			{
				accounttype="Savings";
			}
			else if(choice==2)
			{
				accounttype="Current";
			}
			System.out.println("Enter Minimum Balance To Create Account");
			balance = din.nextDouble();
			if(balance>=500)
			{
				String sql1="insert into customer_account(cid,cus_accountnumber,cus_accounttype,cus_balance,account)values(?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql1);
				ps.setInt(1, cusid);
				ps.setString(2, cnumber);
				ps.setString(3, accounttype);
				ps.setDouble(4, balance);
				ps.setString(5, "Activated");
				int i = ps.executeUpdate();
				if(i>0)
				{
					System.out.println("Dear Customer ... Account is Created Success");
				}
				else
				{
					System.out.println("Dear Customer .. Account Creation Failed");
				}
			}
			else
			{
				System.out.println("Sorry !! minimum 500Rs to Create Account");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	void dashboard()//banking process starts here
	{
		String cnumber;//declaring c number as a customer number using String data type 
		int option,ntxnpass,otxnpass;//initializing the option,new transaction password,old transaction password using integer data type
		System.out.println("*****************************************");//print statement
		System.out.println("****** Welcome To Dashboard********");//print welcome to dash board line
		System.out.println("*****************************************");
		String choice="y";//choice y using string data type
		while(choice.equalsIgnoreCase("y"))//while loop:-it iterates the loop until the given condition gets false
		{
			System.out.println("1.Balance EnQuire");//Print balance enquire
			System.out.println("2.Deposit Cash");//Print deposit cash
			System.out.println("3.WithDrawl Cash");//Print with drawl
			System.out.println("4.Transfer Cash");//Print transfer cash
			System.out.println("5.Change Transaction Password");//Print change password
			System.out.println("6.Exit");//Print exit
			Scanner din = new Scanner(System.in);//user entering  input
			System.out.println("Select Your Process");//print select your process
			option = din.nextInt();
			switch(option)//switch statement 
			{
			case 1:
				System.out.println("******** Balance EnQuire Process*********");//balance enquire process starts here
				System.out.println("Enter Customer Account number");//enter customer account number
				cnumber = din.next();//object creation of customer account number
				try//exception handling technique 
				{
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");//getting connection java over my SQL using jar files
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";//SQL query
					Statement st = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
					ResultSet rs = st.executeQuery(sql);//Executes the given SQL statement, which returns a single ResultSet object. 
					if(rs.next())//if condition
					{
						System.out.println("Enter Transaction Password");//enter transaction password
						ntxnpass = din.nextInt();//new transaction password object creation
						String sql1="select * from customer_account where  transaction_password='"+ntxnpass+"' and cid='"+cusid+"'";//SQL query
						Statement st1 = con.createStatement();//its statement st1 Creates a Statement object for sendingSQL statements to the database
						ResultSet rs1 = st1.executeQuery(sql1);//its result set rs1 Executes the given SQL statement, which returns a single ResultSet object. 
						if(rs1.next())//if condition
						{
							System.out.println("Your Balance Amount is:-  " + rs1.getDouble(4));//print balance amount in the form of double
						}
						else// else block if the above condition is false then print it goes to else block
						{
							System.out.println("Transaction Password is InCorrect");// print Transaction Password is InCorrect
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");//Sorry !!! Customer Account number is InCorrect
					}//close else block
				}//close try block
				catch(Exception e)//exception handling technique if the try block contains any error then catch block statements should be print
				{
					e.printStackTrace();// Prints this throwable and its back trace to the standard error stream Throwable object on the error output stream
				}//close catch block
				break;//break statement
			case 2:
				double damount;//double amount
				System.out.println("******** Deposit Amount Process*********");//deposit process starts here
				System.out.println("Enter Customer Account number");//enter customer account number
				cnumber = din.next();// c number object creation
				try//try block exception handling technique 
				{
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");//getting connection java over my SQL using jar files
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";//SQL query
					Statement st = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
					ResultSet rs = st.executeQuery(sql);//its result set rs1 Executes the given SQL statement, which returns a single ResultSet object. 
					if(rs.next())//if condition
					{
						System.out.println("Enter Transaction Password");//enter transaction password
						ntxnpass = din.nextInt();//new transaction password
						String sql1="select * from customer_account where  transaction_password='"+ntxnpass+"' and cid='"+cusid+"'";//sql1 query
						Statement st1 = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
						ResultSet rs1 = st1.executeQuery(sql1);//its result set rs1 Executes the given SQL statement, which returns a single ResultSet object. 
						if(rs1.next())//if condition  
						{
							double pamount=rs1.getDouble(4);//double amount
							System.out.println("Enter Your Deposit Amount");//print Enter Your Deposit Amount
							damount = din.nextDouble();//object creation d amount
							double tamount = pamount + damount;
							String sql2="update customer_account set cus_balance='"+tamount+"' where cid='"+cusid+"'";
							PreparedStatement ps = con.prepareStatement(sql2);//Creates a PreparedStatement object for sending parameterized SQL statements to the database
							int i=ps.executeUpdate();//Executes the SQL statement in this PreparedStatement object
							if(i>0)//if condition
							{
								System.out.println("Your Amount Deposited in Your Account Successfully");//print Your Amount Deposited in Your Account Successfully"
								//System.out.println("Your Balance is : - " + rs1.getDouble(4));
								// Code Here - Display updated amount //
							}
							else//if condition is not satisfied then else block starts 
							{
								System.out.println("Amount is not Deposited");//print Amount is not Deposited
							}
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");//print Transaction Password is InCorrect
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");//print Sorry !!! Customer Account number is InCorrect
					}
				}
				catch(Exception e)////exception handling technique if the try block contains any error then catch block statements should be print
				{
					e.printStackTrace();// Prints this throwable and its back trace to the standard error stream Throwable object on the error output stream
				}//close catch block
				break;//break statement
			case 3:
				double wamount,gamount;//double data type  w amount,g amount
				System.out.println("******** WithDrawl Amount Process*********");//WithDrawl Amount Process starts here
				System.out.println("Enter Customer Account number");//print Enter Customer Account number"
				cnumber = din.next();//object creation c number
				try//exception handling technique 
				{
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");//getting connection java over my SQL using jar files
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";//SQL query
					Statement st = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
					ResultSet rs = st.executeQuery(sql);//Executes the given SQL statement, which returns a single ResultSet object. 
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");
						ntxnpass = din.nextInt();
						String sql1="select * from customer_account where  transaction_password='"+ntxnpass+"' and cid='"+cusid+"'";
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery(sql1);
						if(rs1.next())
						{
							System.out.println("Enter WithDrawl Amount");
							wamount = din.nextDouble();
							String sql2="select * from customer_account where cid='"+cusid+"'";//SQL query
							Statement st2 = con.createStatement();//Executes the given SQL statement, which returns a single ResultSet object. 
							ResultSet rs2 = st2.executeQuery(sql2);//Executes the given SQL statement, which returns a single ResultSet object. 
							if(rs2.next())//if condition
							{
								gamount = rs2.getDouble(4);
								if(wamount > gamount) // 5001 > 5001 // Code Here minimum Balance maintenance
								{
									System.out.println("Insufficient Balance Amount");
								}
								else
								{
									double uamount = gamount - wamount;
									String sql3="update customer_account set cus_balance='"+uamount+"' where cid='"+cusid+"'";//SQL query
									PreparedStatement ps = con.prepareStatement(sql3);//Creates a PreparedStatement object for sending parameterized SQL statements to the database
									int i= ps.executeUpdate();//print Executes the SQL statement in this PreparedStatement object, 
									if(i>0)//if condition
									{
										System.out.println("Please Wait Your Transaction is Processing ..");//print Please Wait Your Transaction is Processing ..
										System.out.println("Please Take Your Cash");//print Please Take Your Cash
									}
									else//else block
									{
										System.out.println("WithDrawl Failed... Due to Some issues");//print WithDrawl Failed... Due to Some issues
									}
								}
							}
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");//print Transaction Password is InCorrect
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");//print Sorry !!! Customer Account number is InCorrect
					}
				}
				catch(Exception e)// Prints this throwable and its back trace to the standard error stream Throwable object on the error output stream
				{
					e.printStackTrace();
				}//close catch block
				break;//break statement
			case 4:
				String tnumber;
				double tamount;
				System.out.println("******** Transaction Process*********");// Transaction Process starts here
				System.out.println("Enter Customer Account number");//print Enter Customer Account number
				cnumber = din.next();//object creation c number
				try
				{
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");//getting connection java over my SQL using jar files
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";//SQL query
					Statement st = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
					ResultSet rs = st.executeQuery(sql);//Executes the given SQL statement, which returns a single ResultSet object. 
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");//print Enter Transaction Password
						ntxnpass = din.nextInt();
						String sql1="select * from customer_account where  transaction_password='"+ntxnpass+"' and cid='"+cusid+"'";//SQL query
						Statement st1 = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
						ResultSet rs1 = st1.executeQuery(sql1);//Executes the given SQL statement, which returns a single ResultSet object. 
						if(rs1.next())
						{
							System.out.println("Enter Transfer Account Number");//print Enter Transfer Account Number
							tnumber = din.next(); // SBI101
							String sql2="select * from customer_account where cus_accountnumber='"+tnumber+"'";//SQL query
							Statement st2 = con.createStatement();//Creates a Statement object for sendingSQL statements to the database
							ResultSet rs2 = st2.executeQuery(sql2);//Executes the given SQL statement, which returns a single ResultSet object.
							if(rs2.next())
							{
								double vamount = rs2.getDouble(4);
								System.out.println("Transfer Account is Verified");
								System.out.println("Enter Transfer Amount");
								tamount = din.nextDouble();
								String sql3="select * from customer_account where cid='"+cusid+"'";//SQL query
								Statement st3 = con.createStatement();////Creates a Statement object for sendingSQL statements to the database
								ResultSet rs3 = st3.executeQuery(sql3);
								if(rs3.next())
								{
									gamount = rs3.getDouble(4);
									if(tamount > gamount) // 5001 > 5001 // Code Here minimum Balance maintenance
									{
										System.out.println("Insufficient Balance Amount to Transfer");
									}
									else
									{
										double person1 = gamount - tamount;
										double person2 = vamount + tamount;
										String sql4="update customer_account set cus_balance='"+person1+"' where cid='"+cusid+"'";//SQL query
										PreparedStatement ps4 = con.prepareStatement(sql4);//Creates a PreparedStatement object for sending parameterized SQL statements to the database
										int i = ps4.executeUpdate();//Executes the SQL statement in this PreparedStatement object
										String sql5="update customer_account set cus_balance='"+person2+"' where cus_accountnumber='"+tnumber+"'";// SQL query
										PreparedStatement ps5 = con.prepareStatement(sql5);//Creates a PreparedStatement object for sending parameterized SQL statements to the database
										int j = ps5.executeUpdate();//Executes the SQL statement in this PreparedStatement object
										if(i>0 && j>0)
										{
											System.out.println("Transfer Account Successfully Sent");//print Transfer Account Successfully Sent
										}
										else//else block
										{
											System.out.println("Transaction failed");//print Transaction failed
										}
									}
								}
							}
							else
							{
								System.out.println("Transfer Account is not Verified");//print Transfer Account is not Verified
							}
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");//print Transaction Password is InCorrect
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");//print Sorry !!! Customer Account number is InCorrec
					}
				}
				catch(Exception e)//The class Exception and its subclasses are a form of Throwable 
				{
					e.printStackTrace();//Prints this throwable and its back trace to the standard error stream. 
				}
				break;//break statement
			case 5: System.out.println("***** Update / Change Transaction Password*****");//Update / Change Transaction Password starts here
			        System.out.println("Enter Old Transaction Password");
			        otxnpass=din.nextInt();
			        String sql="select * from customer_account where  transaction_password='"+otxnpass+"' and cid='"+cusid+"'";
				try {
					Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank","root","1234");
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next())
					{
						System.out.println("Enter New Transaction Password");
						ntxnpass=din.nextInt();
						String sql1="update customer_account set  transaction_password='"+ntxnpass+"' where cid='"+cusid+"'";
						PreparedStatement ps = con.prepareStatement(sql1);
						int i=ps.executeUpdate();
						if(i>0)
						{
							System.out.println("Successfully Transaction Password is Updated");
						}
						
					}
					else
					{
						System.out.println("Transaction password is not matched");
					}
				} catch (SQLException e)//An exception that provides information on a database access error or other errors
				{

					e.printStackTrace();//Prints this throwable and its back trace to the standard error stream
				}
			        
				break;
			case 6:System.exit(1);
			}
			System.out.println("Do You Want To Continue Press Y Or Exit Press N");
			choice=din.next();
		}
	}
}
