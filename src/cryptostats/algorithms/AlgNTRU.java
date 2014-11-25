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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;

public class AlgNTRU implements ValidAlgorithm{

	private KeySet keySet = null;
	private SecureRandom random;
	private EncryptionKeyPair ssKey;
	private NtruEncrypt ntru = new NtruEncrypt(EncryptionParameters.APR2011_439_FAST);
	private FileAndTimer fat = new FileAndTimer();


	public AlgNTRU(){
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
	         
	        fat.testTimer.startEncTimer();
	        byte[] outputBytes = ntru.encrypt(inputBytes,ssKey.getPublic());
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
	         
	        fat.testTimer.startDecTimer();
	        byte[] outputBytes = ntru.decrypt(inputBytes, ssKey);
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
