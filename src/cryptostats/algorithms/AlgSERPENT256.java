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

import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;

public class AlgSERPENT256 implements ValidAlgorithm{
	
	private KeySet keySet = null;
	private byte[] initVector = null;
	private FileAndTimer fat = new FileAndTimer();
	
	public AlgSERPENT256(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		fat.testTimer =  new TestTimer();
	}
	
	public void generateRandomKeySet() {
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("Serpent","BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			boolean fucked = true;
		}
		keyGen.init(256);//128 192 256 
		SecretKey secretKey = keyGen.generateKey();
		//symmetric so return the same key.
		keySet = new KeySet(secretKey, secretKey);
	}

	@Override
	public FileAndTimer encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			Cipher cipher = Cipher.getInstance("Serpent/CBC/NoPadding","BC");
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
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
		
        
	}

	@Override
	public FileAndTimer decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			Cipher cipher = Cipher.getInstance("Serpent/CBC/NoPadding","BC");
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
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
	}


}
