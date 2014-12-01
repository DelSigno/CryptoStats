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
import javax.crypto.SecretKey;

import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.NtruEncrypt;
import net.sf.ntru.exception.NtruException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;

public class AlgNTRU256 implements ValidAlgorithm{

	private KeySet keySet = null;
	private SecureRandom random;
	private EncryptionKeyPair ssKey;
	private NtruEncrypt ntru = new NtruEncrypt(EncryptionParameters.APR2011_743_FAST);
	private FileAndTimer fat = new FileAndTimer();
	
	private static final int IBLOCKSIZE = 65;
	private static final int ICIPHERTEXTSIZE = 1022;


	public AlgNTRU256(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		random = new SecureRandom();
		fat.testTimer =  new TestTimer();
	}
	
	

	
	@Override
	public void generateRandomKeySet() {
		KeyPairGenerator keyGen = null;
		EncryptionKeyPair kp = ntru.generateKeyPair();
	
		
		ssKey = kp;
		//symmetric so return the same key.
		//keySet = new KeySet(kp.getPrivate(), kp.getPublic());
		
		
	}

	@Override
	public FileAndTimer encryptFile(File fileIn) {
		File fileOut = new File(fileIn.getAbsolutePath()+"_encrypted");

		try {

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        //reads the data from a file into the array of byes inputBytes
	        inputStream.read(inputBytes);
	         
	  
	        
	        //byte[] outputBytes = ntru.encrypt(inputBytes,ssKey.getPublic());

	        
//	        byte[] outputBytes = new byte[(inputBytes.length/65) * 604];
//	        
//	        byte[] tempOutput = new byte[604];
//	        byte[] tempInput = new byte[65];
	        
	        byte[] outputBytes = new byte[(inputBytes.length/IBLOCKSIZE) * ICIPHERTEXTSIZE];
	        byte[] tempOutput = new byte[ICIPHERTEXTSIZE];
	        byte[] tempInput = new byte[IBLOCKSIZE];
	        fat.testTimer.startEncTimer();
	        
	        for(int i = 0; ((i+1)*IBLOCKSIZE) <= inputBytes.length; i++){
	        	//System.out.println("NTRU encrypting block #" + i + " of size: "+ inputBytes.length);
	        	for(int j = 0; j < IBLOCKSIZE; j++){
	        		tempInput[j] = inputBytes[(i*IBLOCKSIZE)+j]; 
	        		
	        	}
	        	

	        	tempOutput = ntru.encrypt(tempInput, ssKey.getPublic());
	        	//outputBytes = ntru.encrypt(inputBytes,ssKey.getPublic());
	        	
	        	
	        	for(int j = 1; (j-1) < ICIPHERTEXTSIZE; j++){
	        		outputBytes[(i*ICIPHERTEXTSIZE)+j-1] = tempOutput[j-1];
	        		
	        	}
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

	         
	        FileInputStream inputStream = new FileInputStream(fileIn);
	        byte[] inputBytes = new byte[(int) fileIn.length()];
	        //Read ths file into an array of bytes
	        inputStream.read(inputBytes);
	         
	        //byte[] outputBytes = ntru.decrypt(inputBytes, ssKey);
	        
	        byte[] outputBytes = new byte[inputBytes.length];
	        int iciphertextsize = inputBytes.length/1000; 
	        byte[] tempOutput = new byte[IBLOCKSIZE];
	        byte[] tempInput = new byte[iciphertextsize];
	        
	        fat.testTimer.startDecTimer();
	        for(int i = 0; ((i+1)*iciphertextsize) <= inputBytes.length; i++){
	        	//System.out.println("NTRU decrypting block #" + i + " of size: "+ inputBytes.length);
	        	
	        	for(int j = 0; (j) < iciphertextsize; j++){
	        		tempInput[j] = inputBytes[(i*iciphertextsize)+j]; 
	        		
	        	}
	        	
	        	//outputBytes = ntru.decrypt(inputBytes, ssKey);
	        	
	        	tempOutput = ntru.decrypt(tempInput, ssKey);
	        	
	        	
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
		} catch (IOException| NtruException e) {
			//e.printStackTrace();
			return fat;
		}
	
	}

}
