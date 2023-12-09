import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect 
{
	static boolean getConnection() throws SQLException //static method
{
		try
		{
		Class.forName("com.mysql.cj.jdbc.Driver");
	   
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return true;
}
	
}
