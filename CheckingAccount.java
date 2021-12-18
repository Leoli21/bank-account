
public class CheckingAccount extends BankAccount
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public final double OVER_DRAFT_FEE;
	public final double TRANSACTION_FEE;
	public final int FREE_TRANS;
	
	private int numTransactions;
	
	public CheckingAccount(String n, double b, double odf, double tf, int freeTrans)
	{
		super(n,b);
		OVER_DRAFT_FEE = odf;
		TRANSACTION_FEE = tf;
		FREE_TRANS = freeTrans;
	}
	
	public CheckingAccount(String n, double odf, double tf, int freeTrans)
	{
		super(n);
		OVER_DRAFT_FEE = odf;
		TRANSACTION_FEE = tf;
		FREE_TRANS = freeTrans;	
	}
	
	public void deposit(double amt) throws IllegalArgumentException
	{
		if(amt <= 0)
		{
			throw new IllegalArgumentException("Deposit Rejected: Cannot Deposit Negative Amount.");
		}
		else
		{
			if(numTransactions >= FREE_TRANS)
			{
				super.deposit(amt);
				super.withdraw(TRANSACTION_FEE);
				numTransactions++;
			}
			else
			{
				super.deposit(amt);
				numTransactions++;
			}
		}
	}
	
	public void withdraw(double amt) throws IllegalArgumentException
	{
		if(amt <= 0) //Illegal Case 1
		{
			throw new IllegalArgumentException("Wtihdraw Rejected: Cannot Withdraw Negative Amount.");
		}
		else if(getBalance() < 0) //Illegal Case 2
		{
			throw new IllegalArgumentException("Withdraw Rejected: Negative Account Balance.");
		}
		else
		{
			double remainingBalance = getBalance()-amt;
			if(remainingBalance <= 0) //OVERDRAFT
			{
				if(numTransactions >= FREE_TRANS) 
				{
					super.withdraw(amt + TRANSACTION_FEE + OVER_DRAFT_FEE);
					numTransactions++;
				}
				else
				{
					super.withdraw(amt + OVER_DRAFT_FEE);
					numTransactions++;
				}
			}
			else //NO OVERDRAFT
			{
				if(numTransactions >= FREE_TRANS)
				{
					super.withdraw(amt + TRANSACTION_FEE);
					numTransactions++;
				}
				else
				{
					super.withdraw(amt);
					numTransactions++;
				}
			}
		}
	}
	
	public void transfer(BankAccount other, double amt) throws IllegalArgumentException
	{
		if(!(getName().equals(other.getName()))) //Illegal Case 1
		{
			throw new IllegalArgumentException("Transfer Rejected: Account Name Mismatch");
		}
		else if(((getBalance() < 0) && (getBalance() != TRANSACTION_FEE)) || (getBalance()-amt) < 0)  //Illegal Case 2
		{
			throw new IllegalArgumentException("Transfer Rejected: Balance in Account Cannot Go Negative");
		}
		else	
		{
			super.transfer(other, amt);
		}
	}
	
	public void endOfMonthUpdate()
	{
		numTransactions = 0;
	}
	
//	@Override
//	public String toString() {
//		return "checkingAccount" + '\t' + this.getAccountNum() + '\t' + this.getName() + '\t' + this.getBalance();
//	}
}
