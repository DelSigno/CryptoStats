package cryptostats.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AlgAES implements ValidAlgorithm {

	public AlgAES(){

	}

	@Override
	public KeySet generateRandomKeySet() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			boolean fucked = true;
		}
		keyGen.init(256); 
		SecretKey secretKey = keyGen.generateKey();
		//symmetric so return the same key.
		return new KeySet(secretKey, secretKey);
	}

	@Override
	public File encryptFile(File fileIn, Object key) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");
		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, (SecretKey)key);
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	@Override
	public File decryptFile(File fileName, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

}
