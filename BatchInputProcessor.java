import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Handles batch input processing using properties file.
 */
public class BatchInputProcessor {

	/**
	 * Run batch input using properties file.
	 * @param filename properties filename
	 */
	public static void run(String filename) {
		String methodName = "";
		ArrayList<String> methodParams = new ArrayList<String>();
		
		try {
			// Extract batch input from property file.
			Properties props = new Properties();
			FileInputStream input = new FileInputStream(filename);
			props.load(input);
			String value = props.getProperty("p1.batch.input");
			// Parse input for method names and parameters.
			String[] tokens = value.split(",");
			for (int i = 0; i < tokens.length; i++) {
				if (tokens[i].charAt(0) == '#' && methodName == "") {
					methodName = tokens[i];
				} else if (tokens[i].charAt(0) == '#' && methodName != "") {
					executeMethod(methodName, methodParams);
					methodName = tokens[i];
					methodParams.clear();
				} else {
					methodParams.add(tokens[i]);
				}
			}
			if (methodName != "") {
				executeMethod(methodName, methodParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute method with name and parameters.
	 * @param methodName name of method to execute
	 * @param methodParams list of parameters to pass to method
	 */
	private static void executeMethod(String methodName, ArrayList<String> methodParams) {		
		switch (methodName) {
		case "#newCustomer":
			BankingSystem.newCustomer(methodParams.get(0), methodParams.get(1), methodParams.get(2), methodParams.get(3));
			break;
		case "#openAccount":
			BankingSystem.openAccount(methodParams.get(0), methodParams.get(1), methodParams.get(2));
			break;
		case "#closeAccount":
			BankingSystem.closeAccount(methodParams.get(0));
			break;
		case "#deposit":
			BankingSystem.deposit(methodParams.get(0), methodParams.get(1));
			break;
		case "#withdraw":
			BankingSystem.withdraw(methodParams.get(0), methodParams.get(1));
			break;
		case "#transfer":
			BankingSystem.transfer(methodParams.get(0), methodParams.get(1), methodParams.get(2));
			break;
		case "#accountSummary":
			BankingSystem.accountSummary(methodParams.get(0));
			break;
		case "#reportA":
			BankingSystem.reportA();
			break;
		case "#reportB":
			BankingSystem.reportB(methodParams.get(0), methodParams.get(1));
			break;
		default:
			System.out.println("Could not find method to execute");
			break;
		}
	}
}
