import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class BankAccountMain 
{
	private static ArrayList <BankAccount> BankAccounts = new ArrayList<BankAccount>();
	public final static double OVER_DRAFT_FEE = 15;
	public final static double RATE = 0.0025;
	public final static double TRANSACTION_FEE = 1.5;
	public final static double MIN_BAL = 300;
	public final static double MIN_BAL_FEE = 10;
	public final static int FREE_TRANSACTIONS = 10;
	
	private static BankAccount getAccountByNumber(int number) //Static method???
	{
		int acctIndex = 0;
		boolean found = false;
		while(acctIndex < BankAccounts.size() && !found)
		{
			if(BankAccounts.get(acctIndex).getAccountNum() == number)
			{
				found = true;
			}
			else
			{
				acctIndex++;
			}
		}
		if(found) 
		{
			return BankAccounts.get(acctIndex);
		}
		else
		{
			return null;
		}
	}
	
	private static ArrayList<BankAccount> getAccountsByName(String name) //Static method?
	{
		ArrayList<BankAccount> AccountsByName = new ArrayList<BankAccount>();
		for(int i = 0; i < BankAccounts.size(); i++)
		{
			if(BankAccounts.get(i).getName().equals(name))
			{
				AccountsByName.add(BankAccounts.get(i));
			}
		}
		if(AccountsByName.size() == 0)
		{
			return null;
		}
		else
		{
			return AccountsByName;
		}
	}
	
	public static void main(String[] args) 
	{
		final String fileName = "Bank Account Information.txt";
		try {
			
			// 1. read from file into memory
			
			// open file
			FileInputStream readFrom = new FileInputStream(fileName);
			
			 //open object stream
			ObjectInputStream readStream = new ObjectInputStream(readFrom);
			BankAccounts = (ArrayList<BankAccount>) readStream.readObject();
			
			
			// close stream
			readStream.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		boolean cont = true;
		Scanner input = new Scanner(System.in);
		while(cont)
		{
			System.out.println("Would you like to add an account, make a transaction, or terminate the program? ('A' to add an account, 'M' to make a transaction, 'T' to terminate the program)");
			String response = input.nextLine();
			while(!(response.equals("A")) && !(response.equals("M")) && !(response.equals("T")))
			{
				System.out.println("Not a valid input. Choose Either 'A' (to add account), 'M' (to make a transaction), or 'T' (to terminate program): ");
				response = input.nextLine();
			}
			if(response.equals("A"))
			{
				System.out.println("Would you like to create a 'Checking' or 'Savings' Account? ");
				String acctType = input.nextLine();
				while(!(acctType.equals("Checking")) && !(acctType.equals("Savings")))
				{
					System.out.println("Not a valid input. Input either 'Checking' or 'Savings': ");
					acctType = input.nextLine();
				}
				if(acctType.equals("Checking"))
				{
					System.out.println("What is the name of the Checking Account? ");
					String checkingName = input.nextLine();
					System.out.println("What is the starting balance? ");

					while(!(input.hasNextDouble()))
					{
						input.nextLine();
						System.out.println("Enter a double value (00.00)");
					}
					double checkingBal = input.nextDouble();
					input.nextLine();
					BankAccounts.add(new CheckingAccount(checkingName,checkingBal, OVER_DRAFT_FEE, TRANSACTION_FEE, FREE_TRANSACTIONS));
				}
				else
				{
					System.out.println("What is the name of the Savings Account? ");
					String savingsName = input.nextLine();
					System.out.println("What is the starting balance? ");
					while(!input.hasNextDouble())
					{
						input.nextLine();
						System.out.println("Enter a double value (00.00)");
					}
					double savingsBal = input.nextDouble();
					input.nextLine();
					BankAccounts.add(new SavingsAccount(savingsName,savingsBal, RATE, MIN_BAL, MIN_BAL_FEE));
				}
			}
			
			else if(response.equals("M"))
			{
				//Prompting and Validating Account Number
				System.out.println("Enter Account Number: ");
				while(!input.hasNextInt())
				{
					input.nextLine();
					System.out.println("Enter an integer Account Number: ");
				}
				int acctNum = input.nextInt();
				input.nextLine();
				while(getAccountByNumber(acctNum) == null)
				{
					System.out.println("Invalid Account Number: Would you like to re-enter account number or would you like to get your account numbers?");
					System.out.println("'R' to Re-Enter or 'G' to Get Your Account Numbers: ");
					String accountNumberResponse = input.nextLine();
					while(!(accountNumberResponse.equals("R")) && !(accountNumberResponse.equals("G")))
					{
						System.out.println("Not a valid input. Choose either 'R' to re-enter account number or 'G' to get account numbers: ");
						accountNumberResponse = input.nextLine();
					}
					if(accountNumberResponse.equals("R"))
					{
						System.out.println("Re-Enter Account Number: ");
						acctNum = input.nextInt();
						input.nextLine();
					}
					else ///Get Account Numbers
					{
						System.out.println("What is your account name? ");
						String name = input.nextLine();
						ArrayList<BankAccount> temp = BankAccountMain.getAccountsByName(name);
						if(temp != null && temp.size() > 0)
						{
							for(int i =0; i <temp.size(); i++)
							{
								if(temp.get(i) instanceof CheckingAccount)
								{
									System.out.println("Checking Account Info:");
									System.out.println(temp.get(i).toString());
								}
								else //Savings Account
								{
									System.out.println("Savings Account Info:");
									System.out.println(temp.get(i).toString());
								}
							}
							System.out.println("Re-Enter Account Number: ");
							acctNum = input.nextInt();
							input.nextLine();
						}
						else {
							System.out.println("Invalid Account Name. Restarting Getting Account Information Process...");
						}
					}
				}
				//Prompting for type of Transaction
				System.out.println("Would you like to Withdraw (W), Deposit(D), Transfer (T), or Get Account Numbers (G)? ");
				String transactionResponse = input.nextLine();
				while(!(transactionResponse.equals("W")) && !(transactionResponse.equals("D")) && !(transactionResponse.equals("T")) && !(transactionResponse.equals("G")))
				{
					System.out.println("Not a valid input. Choose either 'W' to withdraw, 'D' to deposit, 'T' to transfer, or 'G' to get account numbers: ");
					transactionResponse = input.nextLine();
				}
				
				switch(transactionResponse)
				{
				case "W": //Withdraw
					System.out.println("Input desired balance to withdraw: ");
					while(!input.hasNextDouble())
					{
						input.nextLine();
						System.out.println("Enter a double value (00.00)");
					}
					double withdrawAmt = input.nextDouble();
					input.nextLine();
					
					try {
					getAccountByNumber(acctNum).withdraw(withdrawAmt);
					}
					catch(IllegalArgumentException e)
					{
						System.out.println("Transaction Not Authorized");
					}
					break;
					
					
				case "D": //Deposit
					System.out.println("Input desired balance to deposit: ");
					while(!input.hasNextDouble())
					{
						input.nextLine();
						System.out.println("Enter a double value (00.00)");
					}
					double depositAmt = input.nextDouble();
					input.nextLine();
					try {
						getAccountByNumber(acctNum).deposit(depositAmt);
						}
						catch(IllegalArgumentException e)
						{
							System.out.println("Transaction Not Authorized");
						}					
					break;
					
				case "T":
					System.out.println("What account would you like to transfer to? ");
					while(!input.hasNextInt())
					{
						input.nextLine();
						System.out.println("Enter an integer Account Number: ");
					}
					int transferAcctNum = input.nextInt();
					input.nextLine();
					while(getAccountByNumber(transferAcctNum) == null)
					{
						System.out.println("Invalid Account Number: Would you like to re-enter account number or would you like to get your account numbers?");
						System.out.println("'R' to Re-Enter or 'G' to Get Your Account Numbers: ");
						String accountNumberResponse = input.nextLine();
						while(!(accountNumberResponse.equals("R")) && !(accountNumberResponse.equals("G")))
						{
							System.out.println("Not a valid input. Choose either 'R' to re-enter account number or 'G' to get account numbers: ");
							accountNumberResponse = input.nextLine();
						}
						if(accountNumberResponse.equals("R"))
						{
							System.out.println("Re-Enter Account Number: ");
							acctNum = input.nextInt();
							input.nextLine();
						}
						else ///Get Account Numbers
						{
							System.out.println("What is your account name? ");
							String name = input.nextLine();
							ArrayList<BankAccount> temp = BankAccountMain.getAccountsByName(name);
							if(temp != null && temp.size() > 0)
							{
								for(int i =0; i <temp.size(); i++)
								{
									if(temp.get(i) instanceof CheckingAccount)
									{
										System.out.println("Checking Account Info:");
										System.out.println(temp.get(i).toString());
									}
									else //Savings Account
									{
									System.out.println("Savings Account Info:");
									System.out.println(temp.get(i).toString());
									}
								}
								System.out.println("Re-Enter Account Number: ");
								acctNum = input.nextInt();
								input.nextLine();
							}
							else
							{
								System.out.println("Invalid Account Name. Transfer Failed. Restarting Getting Account Information Process.");
							}
						}
					}
					System.out.println("Input desired balance to transfer: ");
					while(!input.hasNextDouble())
					{
						input.nextLine();
						System.out.println("Enter a double value (00.00)");
					}
					double transferAmt = input.nextDouble();
					input.nextLine();
					try {
						getAccountByNumber(acctNum).transfer(getAccountByNumber(transferAcctNum), transferAmt);
						}
						catch(IllegalArgumentException e)
						{
							System.out.println("Transaction Not Authorized");
						}		
					break;
				case "G":
					System.out.println("What is your account name? ");
					String name = input.nextLine();
					ArrayList<BankAccount> temp = BankAccountMain.getAccountsByName(name);			
					if(temp != null && temp.size() > 0)
					{
						for(int i =0; i <temp.size(); i++)
						{
							if(temp.get(i) instanceof CheckingAccount)
							{
								System.out.println("Checking Account Info:");
								System.out.println(temp.get(i).toString());
							}
							else //Savings Account
							{
							System.out.println("Savings Account Info:");
							System.out.println(temp.get(i).toString());
							}
						}
						System.out.println("Re-Enter Account Number: ");
						acctNum = input.nextInt();
						input.nextLine();
					}
					else
					{
						System.out.println("Invalid Account Name.");
					}
					break;
				default: 
					break;
				}
			
			}					
			else
			{							
				cont = false;
				System.out.println("Terminating Program...");
							
				try {
					FileOutputStream bankFile = new FileOutputStream(fileName);
					ObjectOutputStream save = new ObjectOutputStream(bankFile);
					save.writeObject(BankAccounts);
					System.out.println("Successfully wrote to the file.");
					save.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}		
			}
		}
	}		

}
