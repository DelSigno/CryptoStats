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

public class AlgRSA implements ValidAlgorithm{

	private KeySet keySet = null;
	
	public AlgRSA(){
		
	}
	
	@Override
	public void generateRandomKeySet() {
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			boolean fucked = true;
		}
		keyGen.initialize(1024);//512 1024	
		KeyPair secretKey = keyGen.generateKeyPair();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey.getPrivate(), secretKey.getPublic());
		
	}

	@Override
	public File encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, (Key)keySet.getEncKey());
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
        
	}

	@Override
	public File decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
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
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
