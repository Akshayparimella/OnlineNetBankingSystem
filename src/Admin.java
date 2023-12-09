import java.sql.SQLException;//An exception that provides information on a database access  error or other errors.
import java.util.Scanner;//A simple text scanner which can parse primitive types and strings using regular expressions. 

public class Admin //class name
{
public static void main(String[] args) throws SQLException //main method
	{
		int choice;//initializing choice using integer data type
		boolean connection = DbConnect.getConnection();//connection
		if(connection==true)//if condition
		{
			System.out.println("Connection Success"); //print Connection Success
		}
		System.out.println("*********************************");
		System.out.println("Welcome To SBI Banking System");//print Welcome To SBI Banking System
		System.out.println("*********************************");
		System.out.println("Please Choose Your Login");//print Please Choose Your Login
		Scanner din = new Scanner(System.in);//Constructs a new Scanner that produces values scanned from the specified input stream
		System.out.println("1.Admin Login");
		System.out.println("2.Customer Login");//print Customer Login
		choice=din.nextInt();
		switch(choice)//switch statement
		{
		case 1: System.out.println("Admin Login Here");
		        AdminLogIn  al = new AdminLogIn ();//class name object = new class name
				al.LogIn();//object.method name
		break;
		case 2: System.out.println("Customer Login or Register Here");
				CustomerLogIn cl = new CustomerLogIn();//class name object = new class name
				cl.LogType();//object.method name
		break;//break statement
		}//close switch case statements

	}

}
