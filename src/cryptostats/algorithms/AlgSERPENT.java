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
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jcajce.provider.symmetric.Serpent;

public class AlgSERPENT implements ValidAlgorithm{
	
	private KeySet keySet = null;
	private byte[] initVector = null;
	
	public AlgSERPENT(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	public void generateRandomKeySet() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("Serpent","BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			boolean fucked = true;
		}
		keyGen.init(128);//128 192 256 
		SecretKey secretKey = keyGen.generateKey();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey, secretKey);
	}

	@Override
	public File encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("Serpent/CBC/NoPadding","BC");
			cipher.init(Cipher.ENCRYPT_MODE, (Key)keySet.getEncKey());
			initVector = cipher.getIV();
	         
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
			Cipher cipher = Cipher.getInstance("Serpent/CBC/NoPadding","BC");
			cipher.init(Cipher.DECRYPT_MODE, (Key)keySet.getDecKey(), new IvParameterSpec(initVector));
	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
	}


}
