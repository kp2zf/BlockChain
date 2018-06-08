// Kush Patel

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet 
{
	// Fields
	public PrivateKey privateKey; // used to sign transactions
	public PublicKey publicKey; // used as our address
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.

	// Constructor
	public Wallet()
	{
		generateKeyPair();	
	}

	// Method to create a pair of both keys using Elliptic Curve Cryptography
	public void generateKeyPair() 
	{
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			// initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();

			// set the public and private keys from the keyPair 	
			privateKey = keyPair.getPrivate();    	
			publicKey = keyPair.getPublic();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	// returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	public float getBalance() 
	{
		float total = 0;	
		for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet())
		{
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(publicKey)) // if output belongs to me (if coins belong to me)
			{ 
				UTXOs.put(UTXO.id,UTXO); // add it to our list of unspent transactions.
				total += UTXO.value ; 
			}
		}  
		return total;
	}

	// generates and returns a new transaction from this wallet.
	public Transaction sendFunds(PublicKey _recipient,float value) 
	{
		if(getBalance() < value) // gather balance and check funds
		{ 
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		// create array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet())
		{
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) 
				break;
		}

		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);

		for(TransactionInput input: inputs)
			UTXOs.remove(input.transactionOutputId);

		return newTransaction;
	}
}