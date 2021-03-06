package cryptostats.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;

public class AlgAES128crypto implements ValidAlgorithm {

	private KeySet keySet = null;
	private byte[] initVector = null;
	private FileAndTimer fat = new FileAndTimer();

	
	public AlgAES128crypto(){
        fat.testTimer =  new TestTimer();
	}

	@Override
	public void generateRandomKeySet() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			boolean fucked = true;
		}
		keyGen.init(128);//128 192 256 
		SecretKey secretKey = keyGen.generateKey();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey, secretKey);
	}

	@Override
	public FileAndTimer encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, (Key)keySet.getEncKey());
			initVector = cipher.getIV();
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        fat.testTimer.startEncTimer();
	        byte[] outputBytes = cipher.doFinal(inputBytes);
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
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, (Key)keySet.getDecKey(), new IvParameterSpec(initVector));
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        fat.testTimer.startDecTimer();
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	        fat.testTimer.endDecTimer();
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        fat.file = fileOut;
	        return fat;
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
