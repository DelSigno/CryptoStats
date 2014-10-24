package cryptostats.algorithms;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AlgElGamal implements ValidAlgorithm{

	private KeySet keySet = null;
	private SecureRandom random;

	public AlgElGamal(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		random = new SecureRandom();
	}
	
	

	
	@Override
	public void generateRandomKeySet() {
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("ElGamal", "BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			boolean fucked = true;
		}

	
		keyGen.initialize(512);//128 , nor sure if this is good, just default setting	
		KeyPair secretKey = keyGen.generateKeyPair();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey.getPrivate(), secretKey.getPublic());
		
		
	}

	@Override
	public File encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, (Key) keySet.getEncKey(), random);

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public File decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, (Key)keySet.getDecKey());
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
	
	}

}
