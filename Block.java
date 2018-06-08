import java.util.ArrayList;
import java.util.Date;

public class Block 
{
	public String hash;
	public String previousHash;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // our data will be a simple message.	
	private long timeStamp;
	private int nonce;

	// Constructor
	public Block(String previousHash)
	{
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}

	// Method used to calcualte the block's hash
	public String calculateHash()
	{
		return StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
	}

	// Method used to mine block with a certain degree of difficulty
	public void mineBlock(int difficulty)
	{
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = StringUtil.getDificultyString(difficulty); // create a string with difficulty * "0" 
		while(!hash.substring( 0, difficulty).equals(target)) 
		{
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	// add transactions to this block
	public boolean addTransaction(Transaction transaction) 
	{
		// process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((previousHash != "0")) 
		{
			if((transaction.processTransaction() != true)) 
			{
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}