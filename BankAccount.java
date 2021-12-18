import java.io.Serializable;

public abstract class BankAccount implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int nextAccnum = 0;	
	
	private String name;
	private int acctNum;
	private double balance;
	
	public BankAccount(String n)
	{
		name = n;
		acctNum = nextAccnum + 1;
		nextAccnum = acctNum;
		balance = 0;
	}
	public BankAccount(String n, double b)
	{
		name = n;
		acctNum = nextAccnum + 1;
		nextAccnum = acctNum;
		balance = b;
	}
	
	public void deposit(double amt)
	{
		balance += amt;
	}
	
	public void withdraw(double amt)
	{
		balance -= amt;
	}
	
	public int getAccountNum()
	{
		return acctNum;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getBalance()
	{
		return balance;
	}
	
	public abstract void endOfMonthUpdate();
	
	public void transfer(BankAccount other, double amt)
	{
		withdraw(amt);
		other.deposit(amt);
	}
	
	public String toString()
	{
		return "" + acctNum + "\t" + name + "\t$" + balance;
	}

//	public void save(string fileName) {
//		// save to a file
//		
//	}
}
