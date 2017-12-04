import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Author: Yuki Ou
 * Date: 11/20/2017
 * 
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			//con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
			e.printStackTrace();
		}
	}

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) 
	{
		try {
			// inserting customer data
			System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
			String query = "INSERT INTO P1.CUSTOMER (ID,NAME,GENDER,AGE,PIN)" + "VALUES(DEFAULT,?,?,?,?)";
			PreparedStatement pre = con.prepareStatement(query);

			pre.setString(1, name);
			pre.setString(2, gender);
			pre.setString(3, age);
			pre.setString(4, pin);

			pre.execute();
			pre.close();
			
			// retrieving id for customer
			String retrieveID = ("SELECT ID FROM P1.CUSTOMER WHERE NAME = ? AND GENDER = ? AND AGE = ? AND PIN = ?");
			PreparedStatement getID = con.prepareStatement(retrieveID);

			getID.setString(1, name);
			getID.setString(2, gender);
			getID.setString(3, age);
			getID.setString(4, pin);
			rs = getID.executeQuery();
			while (rs.next())
			{
				String idNum = rs.getString(1);
			}
			rs.close();
			getID.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println(":: CREATE NEW CUSTOMER - SUCCESS\n");

	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		try {
			// inserting data into the account table
			System.out.println(":: OPEN ACCOUNT - RUNNING");
			String query = "INSERT INTO P1.ACCOUNT (NUMBER,ID,BALANCE,TYPE,STATUS)" + "VALUES(DEFAULT,?,?,?,?)";
			PreparedStatement pre = con.prepareStatement(query);

			pre.setString(1, id);
			pre.setString(2, amount);
			pre.setString(3, type);
			pre.setString(4, "A");

			pre.execute();
			pre.close();

			// retrieving id for customer
			String retrieveNum = ("SELECT NUMBER FROM P1.ACCOUNT WHERE ID = ?");
			PreparedStatement getNum = con.prepareStatement(retrieveNum);

			getNum.setString(1, id);
			rs = getNum.executeQuery();
			while (rs.next())
			{
				String idNum = rs.getString(1);
				System.out.println(" -- Your Account Number is: " + idNum + " --");
			}	
			rs.close();
			getNum.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println(":: OPEN ACCOUNT - SUCCESS\n");
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		// closing an account, cannot close account for someone else. Condition checked in program launcher.
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		String query = "UPDATE P1.ACCOUNT SET BALANCE = 0, STATUS = 'I' WHERE NUMBER = ?";
		PreparedStatement close;
		try {
			close = con.prepareStatement(query);

			close.setString(1, accNum);

			close.execute();
			close.close();
			//System.out.println("Account Number " + accNum + " has successfully closed. The remaining balance is 0 and the account is currently in an inactive status");
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println(":: CLOSE ACCOUNT - SUCCESS\n");
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		// deposit money into an account. Can deposit for someone else.
		try {
			System.out.println(":: DEPOSIT - RUNNING");
			String query = "UPDATE P1.ACCOUNT SET BALANCE = ? WHERE NUMBER = ?";
			PreparedStatement deposit = con.prepareStatement(query);

			int a1 = Integer.parseInt(balance(accNum));
			int a2 = Integer.parseInt(amount);
			int balance = a1 + a2;
			//System.out.println("Double checking balance: " + balance);
			deposit.setString(1, Integer.toString(balance));
			deposit.setString(2, accNum);

			deposit.execute();
			deposit.close();
			System.out.println();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println(":: DEPOSIT - SUCCESS\n");
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		// withdraw money from the account. Cannot withdraw from someone else's account. Condition checked in program launcher.
		try {
			System.out.println(":: WITHDRAW - RUNNING");
			String query = "UPDATE P1.ACCOUNT SET BALANCE = ? WHERE NUMBER = ?";
			PreparedStatement deposit = con.prepareStatement(query);

			int a1 = Integer.parseInt(balance(accNum));
			int a2 = Integer.parseInt(amount);
			int balance = a1 - a2;

			// check for remaining balance, cannot proceed if the balance is lower than 0.
			if ( balance < 0)
			{
				System.out.println("Your remaining balance cannot be lower than 0!");
			}
			else
			{
				//System.out.println("Double checking balance: " + balance);
				deposit.setString(1, Integer.toString(balance));
				deposit.setString(2, accNum);
			}
			deposit.execute();
			deposit.close();
			System.out.println();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println(":: WITHDRAW - SUCCESS\n");
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		// transfer money into someone else's account. Cannot transfer money from other's account into your account.
		System.out.println(":: TRANSFER - RUNNING");
		deposit(destAccNum, amount);
		withdraw(srcAccNum, amount);
		System.out.println(":: TRANSFER - SUCCESS\n");
	}

	/**
	 * Display account summary.
	 * @param accNum account number
	 */
	public static void accountSummary(String id) 
	{
		// display each account number under the given id and remaining balance
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		String display1 = ("SELECT NUMBER, BALANCE FROM P1.ACCOUNT WHERE ID =? AND STATUS = 'A'");
		//System.out.println("\nDisplaying each account and balance under your id ... ");
		try {
			PreparedStatement displayMsg1 = con.prepareStatement(display1);
			displayMsg1.setString(1, id);
			rs = displayMsg1.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			for (int i = 1; i <= 2; i++)
			{
				System.out.format("%-10s",rsmd.getColumnName(i));
			}
			System.out.println("");
			for (int i = 1; i <= 2; i++)
			{
				System.out.format("%-10s","---------");
			}
			System.out.println("");
			while (rs.next())
			{
				for (int i = 1; i <= columnNum; i++)
				{
					String columnValue = rs.getString(i);
					//System.out.println(rsmd.getColumnName(i) + ": " + columnValue);
					if (columnValue.matches("[a-zA-Z]+"))
					{
						// if letters
						System.out.format("%-10s", columnValue);
					}
					else
					{
						// if numbers
						System.out.format("%9s ", columnValue);
					}
				}
				System.out.println("");
			}
			rs.close();
			displayMsg1.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again. It looks like you either enter an account number that does not belong to you.");
		}

		// display the total balance in the whole account.
//		System.out.println("Your total balance in the account is ..");
		String display2 = ("SELECT sum(BALANCE) AS TOTAL FROM P1.ACCOUNT WHERE ID = ? AND STATUS = 'A'");
//		String totalbalance = "";
		try {
			PreparedStatement displayMsg2 = con.prepareStatement(display2);
			displayMsg2.setString(1, id);
			rs = displayMsg2.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.format("%-10s",rsmd.getColumnName(1));
			System.out.println("");
			System.out.format("%-10s","---------");
			System.out.println("");
			while (rs.next())
			{
				System.out.format("%9s ", rs.getString(1));
			}
			rs.close();
			displayMsg2.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println("\n:: ACCOUNT SUMMARY - SUCCESS");
	}


	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		System.out.println("\n:: REPORT A - RUNNING\n");
		try {
			// create view and the select view statements 
			stmt = con.createStatement();
			stmt.executeUpdate("CREATE OR REPLACE VIEW balanceView as (SELECT ID, SUM(BALANCE) AS TOTAL FROM P1.ACCOUNT GROUP BY ID)");
			String select = ("SELECT DISTINCT balanceView.id, p1.customer.name, p1.customer.age, p1.customer.gender, p1.customer.pin, balanceView.total FROM P1.CUSTOMER, P1.ACCOUNT, balanceView WHERE P1.CUSTOMER.ID = balanceView.id AND P1.ACCOUNT.STATUS = 'A' ORDER BY balanceView.total DESC");
			rs = stmt.executeQuery(select);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			//int columnWid = rsmd.getColumnDisplaySize(column);
			// retrieve total balance from the view
			for (int i = 1; i <= 6; i++)
			{
				System.out.format("%-10s",rsmd.getColumnName(i));
			}
			System.out.println("");
			for (int i = 1; i <= 6; i++)
			{
				System.out.format("%-10s","---------");
			}
			System.out.println("");
			while (rs.next())
			{
				for (int i = 1; i <= columnNum; i++)
				{
					//System.out.printf("%-20s= %s" , rsmd.getColumnName(i));
					String columnValue = rs.getString(i);
					if (columnValue.matches("[a-zA-Z]+"))
					{
						// if letters
						System.out.format("%-10s", columnValue);
					}
					else
					{
						// if numbers
						System.out.format("%9s ", columnValue);
					}
				}
				System.out.println("");
			}
			// drop the view 
			stmt.executeUpdate("DROP VIEW balanceView");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error");
		}
		System.out.println("\n:: REPORT A - SUCCESS\n");
	}

	/**
	 * Display Report B - Average Total Balance Between Age Groups
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
		System.out.println("\n:: REPORT A - RUNNING\n");
		
		// compute the avergae between the given min and max age group
		String age = ("SELECT SUM(BALANCE)/COUNT(DISTINCT P1.ACCOUNT.ID) AS AVERAGE FROM P1.ACCOUNT INNER JOIN P1.CUSTOMER ON P1.CUSTOMER.AGE >= ? AND P1.CUSTOMER.AGE <= ? AND P1.CUSTOMER.ID = P1.ACCOUNT.ID");
		String avg_balance = "";
		try {
			PreparedStatement avg = con.prepareStatement(age);
			avg.setString(1, min);
			avg.setString(2, max);
			rs = avg.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			System.out.format("%-10s", rsmd.getColumnName(1));
			System.out.println("");
			System.out.format("%-10s","---------");
			System.out.println("");
			while (rs.next())
			{
				System.out.format("%9s ", rs.getString(1));
			}
			rs.close();
			avg.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		System.out.println("\n:: REPORT B - SUCCESS\n");
	}

	/**
	 * Return the remaining balance in an account
	 * @param accNum the account number
	 * @return balance the remaining balance
	 */
	public static String balance(String accNum)
	{
		String balance = "";
		try {
			String query = ("SELECT BALANCE FROM P1.ACCOUNT WHERE NUMBER = ?");
			PreparedStatement money = con.prepareStatement(query);

			money.setString(1, accNum);
			rs = money.executeQuery();
			while (rs.next())
			{
				String balance2 = rs.getString(1);
				balance = balance2;
				//System.out.println(" -- Original balance: " + balance2 + " --");
			}	
			rs.close();
			money.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		return balance;
	}

	/**
	 * Get the id of a given account number.
	 * @param accNum account number
	 * @return id the customer's id
	 */
	public static String getId(String accNum)
	{
		// find the appropriate id for the given accNum since there might be multiple accNum under one id
		String id = "";
		String retrieve = ("SELECT ID FROM P1.ACCOUNT WHERE NUMBER =?");
		try {
			PreparedStatement idnum = con.prepareStatement(retrieve);
			idnum.setString(1, accNum);
			rs = idnum.executeQuery();
			while (rs.next())
			{
				id = rs.getString(1);
			}
			rs.close();
			idnum.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		return id;
	}


	/**
	 * Authenticate a user's id and password
	 * @param id User's id
	 * @param pw password
	 */
	public static Boolean logIn(String id, String pw)
	{
		Boolean result = false;
		try {
			String retrieve = ("SELECT ID, PIN FROM P1.CUSTOMER WHERE ID = ? AND PIN =?");
			PreparedStatement idpw = con.prepareStatement(retrieve);

			idpw.setString(1, id);
			idpw.setString(2, pw);
		
			rs = idpw.executeQuery();
			while (rs.next()){
				String authenticateID = rs.getString(1);
				String authenticatePW = rs.getString(2);
				if (authenticateID.equals(id) && authenticatePW.equals(pw)){
					result = true;}
				else{
					result = false;}}
			rs.close();
			idpw.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		return result;
	}

	/**
	 * Authenticate the user, check if the user is actually the user.
	 * @param id user's id (authenticated during log in)
	 * @param accountNum accountNum that the user put in
	 * @return boolean 
	 */
	public static Boolean authenticateUser(String id, String accountNum)
	{
		Boolean result = false;
		String retrieve = ("SELECT ID FROM P1.ACCOUNT WHERE NUMBER =?");
		try {
			PreparedStatement idnum = con.prepareStatement(retrieve);

			idnum.setString(1, accountNum);

			rs = idnum.executeQuery();
			while (rs.next()){
				String authenticateID = rs.getString(1);
				if ( authenticateID.equals(id)){
					result = true;}
				else{
					result = false;}}
			rs.close();
			idnum.close();
		} catch (SQLException e) {
			System.out.println("Error: Please check your input again.");
		}
		return result;
	}
	
	/**
	 * Close the database connection.
	 */
	public void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Unable to close connection");
		}
	}
}
