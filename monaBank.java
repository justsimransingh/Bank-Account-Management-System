package monabank;
import java.sql.*;
import java.util.Scanner;
import java.util.Random;
class monabank
{
        
	static int accNo,valid,pin,correctPin;
	static String spin;
	static int cash = 0;
	public static void main(String [] args) throws Exception
	{
                Class.forName("com.mysql.jdbc.Driver");
		Scanner sc = new Scanner(System.in);
		System.out.println("\nWelcome To Mona Bank\n");
		System.out.println("Please Choose One Of The Services:");
		boolean check = true;
		do 
		{
         
			System.out.println("\n1.Create Account\n2.Check Balance\n3.Deposit Cash\n4.Withdraw Cash\n5.Access admin features\n6.Exit");
			int service = sc.nextInt();
			System.out.println();
			monabank ob = new monabank();
			if ( service == 1)
				ob.createAccount();
			else if ( service == 2)
				ob.checkBalance();
			else if ( service == 3)
				ob.depositCash();
			else if ( service == 4)
				ob.withdrawCash();
			else if ( service == 6)                        
				check = false;
                        else if ( service == 5 )
                                ob.admin();
			else                        
				System.out.println("\nError:Invalid Service\nEnter A Valid Service.Thank You!");                                
                        
		}while(check);
	}
	void createAccount() throws Exception
	{
            
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Your Name-");
		String name = sc.nextLine();
		Random r = new Random();
		accNo = r.nextInt(999999)+100000;
		valid = accNo;
		System.out.println("Your Account Number Is-" + accNo);
		int temp=1;
		for (int i=0;i<3;i++)
		{
			System.out.print("Enter Your PIN-");
			pin = sc.nextInt();
			String spin=String.valueOf(pin);
			if (spin.length()==4)
			{
				correctPin = pin;
				temp=0;
                                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:1999/bank","root","simran");
                                PreparedStatement ps=con.prepareStatement("INSERT INTO data VALUES (?,?,?)");
                                ps.setInt(1,accNo);
                                ps.setInt(2,pin);
                                ps.setInt(3,cash);
                                ps.executeUpdate();
                                con.close();
				break;
			}
			else
				System.out.println("\nError:Invalid Pin\nEnter a 4 digit pin.Thank You!");
		}
		if (temp==1)
		{
			System.out.println("Error:Out Of Attempts\nThank You!");
			System.exit(0);
		}
	}
	void checkBalance() throws Exception
	{
            	Scanner sc = new Scanner(System.in);
		System.out.print("Enter Your Account Number-");
		accNo = sc.nextInt();
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:1999/bank","root","simran");
		Statement st=con.createStatement();
                ResultSet rs=st.executeQuery("SELECT cash FROM data WHERE accNo=" + accNo);
                while (rs.next())
                {
                    System.out.println("\nCash=" + rs.getInt(1));
                }
                con.close();
        }           
	void depositCash() throws Exception
	{
            	Scanner sc = new Scanner(System.in);
		System.out.print("Enter Your Account Number-");
		accNo = sc.nextInt();
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:1999/bank","root","simran");
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery("SELECT cash FROM data WHERE accNo=" + accNo);
                while (rs.next())
                {
                    cash=rs.getInt(1);
                }                
                System.out.print("Enter Amount You Want To Deposit-");
		int addCash = sc.nextInt();                
		cash+=addCash;
		System.out.println("Your Current Account Balance-" + cash);
                PreparedStatement ps=con.prepareStatement("UPDATE data SET cash=(?) WHERE accNo=(?)");
                ps.setInt(1,cash);
                ps.setInt(2,accNo);
                ps.executeUpdate();                                    
                con.close();
        }
	void withdrawCash() throws Exception
	{
            
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Your Account Number-");
		accNo = sc.nextInt();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:1999/bank","root","simran");
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery("SELECT cash FROM data WHERE accNo=" + accNo);
                while (rs.next())
                {
                    cash=rs.getInt(1);
                }
                System.out.print("Enter Amount You Want To Withdraw-");
		int withCash = sc.nextInt();
                if ( cash > withCash)
                {cash-=withCash;
		System.out.println("Your Current Account Balance-" + cash);
                PreparedStatement ps=con.prepareStatement("UPDATE data SET cash=(?) WHERE accNo=(?)");
                ps.setInt(1,cash);
                ps.setInt(2,accNo);
                ps.executeUpdate();}
                else
                    System.out.println("You don't have sufficient balance in your account");
                con.close();
	}
        void admin() throws Exception
        {
                int adminPassword=2606;
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter password to access Admin features:");
                int password = sc.nextInt();
                if (password == adminPassword)
                {
                    System.out.println("\nSuccessfully entered as an Admin\n");
                    System.out.println("Please Choose the operation you want to perform:");
                    boolean check = true;
		do 
		{
         
			System.out.println("\n1.Show full data\n2.Show Account Numbers\n3.Show Total Cash\n4.Exit");
			int service = sc.nextInt();                      
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:1999/bank","root","simran");
                        Statement st=con.createStatement();
                        if ( service == 1 )
                        {                            
                            ResultSet rs=st.executeQuery("SELECT * FROM data");
                            while (rs.next())
                            {
                                System.out.println("AccNo-> " + rs.getInt(1) + "  Pin-> " + rs.getInt(2) + "  Cash-> " + rs.getInt(3));
                            }
                        }                           
                        else if ( service == 2 )
                        {
                            ResultSet rs=st.executeQuery("SELECT accNO FROM data");
                            while (rs.next())
                            {
                                System.out.println(rs.getInt(1));
                            }
                        }
                        else if ( service == 3 )
                        {
                            ResultSet rs=st.executeQuery("SELECT SUM(cash) FROM data");
                            while (rs.next())
                            {
                                System.out.println(rs.getInt(1));
                            }
                        }
                        else if ( service == 4)                        
				check = false;                        
			else                        
				System.out.println("\nError:Invalid Service\nEnter A Valid Service.Thank You!");  
		}while(check);
                }
                
                   
        }
}