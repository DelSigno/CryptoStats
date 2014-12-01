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

import cryptostats.data.FileAndTimer;
import cryptostats.data.FileGeneratorHelper;
import cryptostats.simonspeck.SimonEngine;
import cryptostats.timeing.TestTimer;

public class AlgSIMON128_64 implements ValidAlgorithm{

	
	private KeySet keySet = null;
	/**
	 * We use a 128 bit block and key to remain consistant with AES and Serpent
	 */
	private SimonEngine engine = new SimonEngine(64);
	private FileAndTimer fat = new FileAndTimer();
	
	public AlgSIMON128_64(){
		fat.testTimer =  new TestTimer();
	}
	@Override
	public void generateRandomKeySet() {
		byte[] tempKey = FileGeneratorHelper.generateRandomByteArray(128/8);
		keySet = new KeySet(tempKey,tempKey);
		
	}

	@Override
	public FileAndTimer encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {
			engine.init(true, new KeyParameter((byte[])keySet.getEncKey()));

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = new byte[inputBytes.length];
	        
	        //byte[] tempOutput = new byte[16];
	        //byte[] tempInput = new byte[16];
	        
	        fat.testTimer.startEncTimer();
	        for(int i = 0; ((i+1)*8) <= inputBytes.length; i++){
	        	/*System.out.println("Simon encrypting Block #" + (i+1));
	        	for(int j = 0; j < 16; j++){
	        		tempInput[j] = inputBytes[(i*16)+j]; 
	        		
	        	}*/
	        	
	        	engine.processBlock(inputBytes,(i*8),outputBytes,(i*8));
	        	
	        	/*for(int j = 0; j < 16; j++){
	        		outputBytes[(i*16)+j] = tempOutput[j];
	        		
	        	}*/
	        }   
	        fat.testTimer.endEncTimer();
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        fat.file = fileOut;
	        return fat;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FileAndTimer decryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_decrypted");

		try {
			engine.init(false, new KeyParameter((byte[])keySet.getDecKey()));

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        inputStream.read(inputBytes);
	         
	        byte[] outputBytes = new byte[inputBytes.length];
	        
	        fat.testTimer.startDecTimer();
	        for(int i = 0; ((i+1)*8) <= inputBytes.length; i++){
	        	
	        	engine.processBlock(inputBytes,(i*8),outputBytes,(i*8));
	        	
	        } 
	        fat.testTimer.endDecTimer();
	         
	        FileOutputStream outputStream = new FileOutputStream(fileOut);
	        outputStream.write(outputBytes);
	         
	        inputStream.close();
	        outputStream.close();
	        
	        fat.file = fileOut;
	        return fat;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
