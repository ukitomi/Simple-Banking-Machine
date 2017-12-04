import java.util.Scanner;

/**
 * Author: Yuki Ou
 * Date: 11/20/2017
 * 
 * Main entry to program.
 */
public class ProgramLauncher {

	private static BankingSystem bank;
	private static ProgramLauncher launcher;

	public static void main(String argv[]) {
		System.out.println(":: PROGRAM START");

		if (argv.length < 1) {
			System.out.println("Need database properties filename");
		} else {
			BankingSystem.init(argv[0]);
			BankingSystem.testConnection();
			BatchInputProcessor.run(argv[0]); // test file
		}
		// initialization
		launcher = new ProgramLauncher();
		bank = new BankingSystem();

		// Start the bank. Go to screen one.
		launcher.mainMenu();
	}

	private void mainMenu() {
		Scanner in = new Scanner(System.in);
		boolean run = true;
		try {
			while (run) {
				System.out.format("%50s",
						"  --------------------------------------\n  --------------------------------------");
				System.out.format("%50s", "\n|| Welcome to the Self Service Banking System! - Main Menu");
				System.out.format("%50s",
						"\n  --------------------------------------\n  --------------------------------------");
				System.out.println("\n1. New Customer\n2. Customer Login\n3. Exit\nNote: NUMBER ONLY!");
				int response = in.nextInt();
				if (response == 1) {
					System.out.print(
							"Thank you for choosing us! \nPlease go ahead and provide your information: name?\n=> ");
					in.nextLine();
					String fullname = in.nextLine();
					if (!fullname.matches("[a-zA-Z]+")) {
						System.out.println("Sorry, your name cannot contain numeric values. Please retry");
						continue;
					}
					System.out.print("Gender? F or M only!\n=> "); // add an
																	// exception
					String gender = in.nextLine();
					boolean check1 = gender.startsWith("F");
					boolean check2 = gender.startsWith("M");
					if (gender.length() > 1) {
						System.out.println("Only one letter allow!");
						continue;
					} else if (!check1 && !check2) {
						System.out.println("Gender can only be either F or M. Please retry");
						continue;
					}
					System.out.print("You Age?\n=> ");
					String age = in.nextLine();
					if (Integer.parseInt(age) < 0) {
						System.out.println("Your age cannot be lower than 0. Please retry");
						continue;
					}
					System.out.print("Last but not least.. your pin?\n=> ");
					String pin = in.nextLine();
					if (Integer.parseInt(pin) < 0) {
						System.out.println("Your pin cannot be lower than 0. Please retry");
						continue;
					}
					bank.newCustomer(fullname, gender, age, pin);
				} else if (response == 2) {
					run = false;
					customerLogIn(bank);
				} else if (response == 3) {
					run = false;
					bank.closeConnection();
					System.out.println("Thank you for choosing us. Have a great day!");
				} else {
					System.out.println("Error.");
				}
			}
		} catch (Exception e) {
			System.out.println("Please only type in one digit at a time!\nIf you wish to exit the program, press 3.");
		} finally {
			mainMenu();
		}

		in.close();
	}

	private void customerLogIn(BankingSystem bank) {
		Scanner in = new Scanner(System.in);
		System.out.format("%50s", "  --------------------------------------\n  --------------------------------------");
		System.out.format("%50s", "\n|| Customer Log in Page");
		System.out.format("%50s",
				"\n  --------------------------------------\n  -------------------------------------- \n");
		try {
			System.out.print("Please enter your id\n=> ");
			String customerId = in.nextLine();
			System.out.print("Please enter your password\n=> ");
			String customerPw = in.nextLine();
			if (customerId.equals("0") && customerPw.equals("0")) {
				// proceed to admin page
				adminPage(bank);
			} else if (bank.logIn(customerId, customerPw) == true) {
				// proceed to screen #3
				String passId = customerId + "";
				customerMainMenu(bank, passId);
			} else {
				System.out.println("Fail to authenticate. Returning back to previous menu ...");
				mainMenu();
			}
		} catch (Exception e) {
			System.out.println("Error: Numeric values only.");
			mainMenu();
		}
		in.close();
	}

	private void customerMainMenu(BankingSystem bank, String id) {
		boolean run = true;
		Scanner in = new Scanner(System.in);
		while (run) {
			System.out.format("%50s",
					"  --------------------------------------\n  --------------------------------------");
			System.out.format("%50s", "\n|| Customer Main Menu");
			System.out.format("%50s",
					"\n  --------------------------------------\n  -------------------------------------- \n");
			System.out.println(
					"1. Open Account\n2. Close Account\n3. Deposit\n4. Withdraw\n5. Transfer\n6. AccountSummary\n7. Exit");
			int response = in.nextInt();
			if (response == 1) {
				// open account
				// prompt for id, account type, and amount of money deposit
				in.nextLine();
				System.out.print("You chose to open an account.\n May I have your id?\n=> ");
				String userId = in.nextLine();
				System.out.print("What type of account you want? Enter C for checking, S for saving.\n=> ");
				String userType = in.nextLine();
				if (!userType.equals("C") && !userType.equals("S")) {
					System.out.println("Your account type can either be Checking or Saving. Please retry.");
					continue;
				}
				System.out.print("How many amount you want to deposit? It has to be greater than 0.\n=> ");
				String userAmount = in.nextLine();
				if (Integer.parseInt(userAmount) < 0) {
					System.out.println("The amount you deposit cannot be lower than 0. Please retry.");
					continue;
				}
				bank.openAccount(userId, userType, userAmount);
			} else if (response == 2) {
				// close account
				// prompt for account number, cannot close account for someone
				// else
				System.out.print(
						"You chose to close an account.\nMay I have the account number that you wish to close?\n=> ");
				String userNum = in.next();
				// authenticate if it's the right user
				if (bank.authenticateUser(id, userNum) == true) {
					System.out.println("Authenticated");
					bank.closeAccount(userNum);
				} else {
					System.out.println("You cannot close an account for someone else!");
				}
			} else if (response == 3) {
				// deposit money
				// prompt for account number and amount wish to deposit. Can
				// deposit into anyone's account
				System.out.print(
						"You chose to deposit..\nMay I have the account number that you wish to deposit into?\n=> ");
				String userNum = in.next();
				System.out.println("How much money you wish to deposit?=> ");
				String userAmount = in.next();
				bank.deposit(userNum, userAmount);
			} else if (response == 4) {
				// withdraw money
				// prompt for account number and amount wish to withdraw. Cannot
				// withdraw from someone else's account
				System.out.print(
						"You chose to withdraw..\nMay I have the account number that you wish to withdraw from?\n=> ");
				String userNum = in.next();
				System.out.print("How much money you wish to withdraw?\n=> ");
				String userAmount = in.next();
				// authenticate if withdrawing from the right account.
				if (bank.authenticateUser(id, userNum) == true) {
					System.out.println("Authenticated");
					bank.withdraw(userNum, userAmount);
				} else {
					System.out.println("You cannot withdraw money from someone else's account!");
				}
			} else if (response == 5) {
				// transfer money
				// prompt for sourceAccountNumber, destinationAccountNumber, and
				// amount wish to transfer.
				System.out.print("You chose to transfer money..\nMay I have your account number?\n=> ");
				String srsAcc = in.next();
				System.out.print("May I have the other person's account number?\n=> ");
				String destAcc = in.next();
				System.out.print("How much money you wish to transfer?\n=> ");
				String userAmount = in.next();
				// since cannot withdraw from someone else's account and deposit
				// into yours
				// authenticate required.
				if (bank.authenticateUser(id, srsAcc) == true) {
					System.out.println("Authenticated");
					bank.transfer(srsAcc, destAcc, userAmount);
				} else {
					System.out.println("You cannot withdraw money from someone else's account!");
				}
			} else if (response == 6) {
				// Display Account Summary
				// prompt for account number
				System.out.print(
						"You chose to display your account summary.\nMay I have your one of your account number?\n=> ");
				String userAcc = in.next();
				if (bank.authenticateUser(id, userAcc) == true) {
					bank.accountSummary(bank.getId(userAcc));
				} else {
					System.out.println("You cannot check on someone else's account!");
				}
			} else if (response == 7) {
				// exit
				run = false;
				mainMenu();
			} else {
				System.out.println("Error. Please rerun the command again. Remember, only single digit allowed!");
			}
		}
		in.close();
	}

	private void adminPage(BankingSystem bank) {
		Scanner in = new Scanner(System.in);
		boolean run = true;
		while (run) {
			System.out.format("%50s",
					"  --------------------------------------\n  --------------------------------------");
			System.out.format("%50s", "\n|| Title – Administrator Main Menu");
			System.out.format("%50s",
					"\n  --------------------------------------\n  -------------------------------------- \n");
			System.out.println(
					"1. Account Summary for a Customer\n2. Report A :: Customer Information with Total Balance in Decreasing Order\n3. Report B :: Find the Average Total Balance Between Age Groups\n4. Exit");
			int response = in.nextInt();
			if (response == 1) {
				// account summary for a customer
				System.out.print("Type the id of the customer that you want to display summary of\n=> ");
				String id = in.next();
				bank.accountSummary(id);
			} else if (response == 2) {
				// report A: report for each customer and their total balance
				bank.reportA();
			} else if (response == 3) {
				// report B: report for an average total balance between age
				// group.
				System.out.print(
						"You chose to compute the avergae total balance between age group.\nPlease input the minimum age\n=> ");
				String min = in.next();
				System.out.print("\nMaximum age?\n=> ");
				String max = in.next();

				if (Integer.parseInt(min) > Integer.parseInt(max)) {
					System.out.println("The minimum age cannot be greater than the maximum age! Please retry.");
					continue;
				} else if (Integer.parseInt(max) < Integer.parseInt(min)) {
					System.out.println("The maximum age cannot be lower than the minimum age! Please retry.");
					continue;
				}
				bank.reportB(min, max);
			} else if (response == 4) {
				// exit
				run = false;
				mainMenu();
			} else {
				System.out.println("Error!");
			}
		}
		in.close();
	}
}
