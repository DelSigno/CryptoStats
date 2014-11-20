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
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import cryptostats.data.FileGeneratorHelper;
import cryptostats.simonspeck.SimonEngine;

public class AlgSIMON implements ValidAlgorithm{

	
	private KeySet keySet = null;
	/**
	 * We use a 128 bit block and key to remain consistant with AES and Serpent
	 */
	private SimonEngine engine = new SimonEngine(128);
	public AlgSIMON(){
		
	}
	@Override
	public void generateRandomKeySet() {
		byte[] tempKey = FileGeneratorHelper.generateRandomByteArray(128/8);
		keySet = new KeySet(tempKey,tempKey);
		
	}

	@Override
	public File encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			engine.init(true, new KeyParameter((byte[])keySet.getEncKey()));

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = new byte[inputBytes.length];
	        engine.processBlock(inputBytes,0,outputBytes,0);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public File decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			engine.init(false, new KeyParameter((byte[])keySet.getDecKey()));

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = new byte[inputBytes.length];
	        engine.processBlock(inputBytes,0,outputBytes,0);
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        return fileOut;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
