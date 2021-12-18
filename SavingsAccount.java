
public class SavingsAccount extends BankAccount 
{
	/**
	 * 
	 */
	private static final long serialVersionUID =3L;
	private double intRate;
	private int numTransfers;
	
	public final double MIN_BAL;
	public final double MIN_BAL_FEE;
	public final static int MAX_TRANSFER = 6;
	public final static double TRANSACTION_FEE = 10;
	
	public SavingsAccount(String n, double b, double r, double mb, double mbf)
	{
		super(n,b);
		intRate = r;
		MIN_BAL = mb;
		MIN_BAL_FEE = mbf;
	}
	
	public SavingsAccount(String n, double r, double mb, double mbf)
	{
		super(n);
		intRate = r;
		MIN_BAL = mb;
		MIN_BAL_FEE = mbf;
	}
	
	public void withdraw(double amt) throws IllegalArgumentException
	{
		if(amt <= 0)
		{
			throw new IllegalArgumentException("Wtihdraw Rejected: Cannot Withdraw Negative Amount.");		
		}
		else if(getBalance()-amt < 0 && ( (getBalance() != MIN_BAL_FEE) || (getBalance() != TRANSACTION_FEE) || (getBalance() != MIN_BAL_FEE + TRANSACTION_FEE)) ) 
		{
			throw new IllegalArgumentException("Withdraw Rejected: Account Balance Cannot Go Negative.");
		}
		else
		{
			if((getBalance()-amt) < MIN_BAL)
			{
				super.withdraw(amt + MIN_BAL_FEE);
			}
			else
			{
				super.withdraw(amt);
			}
		}
		
	}
	public void deposit(double amt) throws IllegalArgumentException
	{
		if(amt <= 0)
		{
			throw new IllegalArgumentException("Deposit Rejected: Cannot Deposit Negative Amount");
		}
		else
		{
			super.deposit(amt);
		}
	}
	public void transfer(BankAccount other, double amt) throws IllegalArgumentException
	{
		if(!(getName().equals(other.getName()))) 
		{
			throw new IllegalArgumentException("Transfer Rejected: Account Name Mismatch");
		}
		else
		{
			if(other instanceof CheckingAccount)
			{
				CheckingAccount other2 = (CheckingAccount) other;
				if(numTransfers>= MAX_TRANSFER)
				{
					withdraw(amt + TRANSACTION_FEE);
					other2.deposit(amt);
					numTransfers++;
				}
				else
				{
					withdraw(amt);
					other2.deposit(amt);
					numTransfers++;
				}
			}
			else
			{
				if(numTransfers >= MAX_TRANSFER)
				{
					withdraw(amt + TRANSACTION_FEE);
					other.deposit(amt);
					numTransfers++;
				}
				else
				{
					withdraw(amt);
					other.deposit(amt);
					numTransfers++;
				}
			}
		}
	}
	
	public void addInterest()
	{
		double interestBalance = getBalance() * intRate;
		deposit(interestBalance);
	}
	public void endOfMonthUpdate()
	{
		//Adds interest??
		numTransfers = 0;
	}
	

//	public String toString() {
//		return "savingAccount" + '\t' + this.getAccountNum() + '\t' + this.getName() + '\t' + this.getBalance();
//	}
}
