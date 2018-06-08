// Kush Patel

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;

import com.google.gson.GsonBuilder;

public class StringUtil 
{
	// Creating a helper method that applies the SHA256 algorithm to a String and returns a reuslt
	public static String applySha256(String input)
	{
		try {			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8")); // apply SHA256 to input
			StringBuffer hexString = new StringBuffer(); // will contain hash as a hexadecimal
			for (int i = 0; i < hash.length; i++) // populate the StringBuffer
			{
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) 
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();				
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}
	
	//Short hand helper to turn Object into a json string
		public static String getJson(Object o) 
		{
			return new GsonBuilder().setPrettyPrinting().create().toJson(o);
		}
		
		//Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"  
		public static String getDificultyString(int difficulty) 
		{
			return new String(new char[difficulty]).replace('\0', '0');
		}

	//Verifies a String signature 
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	//Tacks in array of transactions and returns a merkle root.
	public static String getMerkleRoot(ArrayList<Transaction> transactions) 
	{
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.transactionId);
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
}
