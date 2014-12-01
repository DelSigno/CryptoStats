package cryptostats.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;

public class AlgRSA512 implements ValidAlgorithm{

	private KeySet keySet = null;
	private FileAndTimer fat = new FileAndTimer();
	
	private static final int KEYSIZE = 512;
	private static final int IBLOCKSIZE = 53;
	private static final int ICIPHERTEXTSIZE = 64;
	
	public AlgRSA512(){
		fat.testTimer =  new TestTimer();
	}
	
	@Override
	public void generateRandomKeySet() {
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			boolean fucked = true;
		}
		keyGen.initialize(KEYSIZE);//512 1024 2048 4096 8192 16384
		KeyPair secretKey = keyGen.generateKeyPair();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey.getPrivate(), secretKey.getPublic());
		
	}

	@Override
	public FileAndTimer encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, (Key)keySet.getEncKey());
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	        
	        byte[] outputBytes = new byte[(inputBytes.length/IBLOCKSIZE) * ICIPHERTEXTSIZE];
	        byte[] tempOutput = new byte[IBLOCKSIZE];
	        byte[] tempInput = new byte[ICIPHERTEXTSIZE];
	         
	        fat.testTimer.startEncTimer();
	        for(int i = 0; ((i+1)*ICIPHERTEXTSIZE) <= inputBytes.length; i++){
	        	//System.out.println("NTRU decrypting block #" + i + " of size: "+ inputBytes.length);
	        	
	        	for(int j = 0; (j) < ICIPHERTEXTSIZE; j++){
	        		tempInput[j] = inputBytes[(i*ICIPHERTEXTSIZE)+j]; 
	        		
	        	}
	        	tempOutput = cipher.doFinal(tempInput);
	
	        	for(int j = 0; j < IBLOCKSIZE; j++){
	        		outputBytes[(i*IBLOCKSIZE)+j] = tempOutput[j];
	        		
	        	}
	        }  
	        fat.testTimer.endEncTimer();
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        fat.file = fileOut;
	        return fat;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
        
	}

	@Override
	public FileAndTimer decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, (Key)keySet.getDecKey());
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	        byte[] outputBytes = new byte[inputBytes.length];       
	        byte[] tempOutput = new byte[IBLOCKSIZE];
	        byte[] tempInput = new byte[ICIPHERTEXTSIZE];
	        
	        fat.testTimer.startDecTimer();
	        for(int i = 0; ((i+1)*ICIPHERTEXTSIZE) <= inputBytes.length; i++){
	        	
	        	
	        	for(int j = 0; (j) < ICIPHERTEXTSIZE; j++){
	        		tempInput[j] = inputBytes[(i*ICIPHERTEXTSIZE)+j]; 
	        		
	        	}
	        	tempOutput = cipher.doFinal(tempInput);
	        	for(int j = 0; j < IBLOCKSIZE; j++){
	        		outputBytes[(i*IBLOCKSIZE)+j] = tempOutput[j];
	        		
	        	}
	        }  
	        fat.testTimer.endDecTimer(); 
	        
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        fat.file = fileOut;
	        return fat;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
