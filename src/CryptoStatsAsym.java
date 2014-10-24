import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.StringTokenizer;

import cryptostats.algorithms.AlgAES;
import cryptostats.algorithms.AlgElGamal;
import cryptostats.algorithms.AlgRSA;
import cryptostats.algorithms.KeySet;
import cryptostats.algorithms.ValidAlgorithm;
import cryptostats.timeing.TestTimer;
import NIST.MCT;


public class CryptoStatsAsym {


	//private static final String ALGNAME = "AlgRSA";
	private static final String ALGNAME = "AlgElGamal";
	
	//ValidAlgorithm algorithmInstance = new AlgRSA();
	ValidAlgorithm algorithmInstance = new AlgElGamal();
	
	private static final String INITIALFILENAME = "rsatest";
	private static final int PLAINTEXTLENGTH = 112;//in bytes
	private static final long NANOCONVERTER = 1000000000L;
	

	public static void main(String[] args) {
		
		CryptoStatsAsym meta = new CryptoStatsAsym();
		TestTimer timer = meta.testFileTime(PLAINTEXTLENGTH);

		System.out.println("Encryption time: " + ((double)timer.getEncTime())/NANOCONVERTER + "\n");
		System.out.println("Decryption time: " + ((double)timer.getDecTime())/NANOCONVERTER + "\n");
		System.out.println("Total time: " + ((double)timer.getTotalTime())/NANOCONVERTER + "\n");

		
	}
	
	/**
	 * Some variable we may use
	 */
	private String dirName = System.getProperty("user.dir");       // Used for code conversion
	private File destination = null;     // destination directory File object
	private String cipherName = null; // cipher algorithm name, probably wont use
	


	
   
	private TestTimer testFileTime(int fileSize) {

		//Generate random Plaintext
		File fileToEncrypt = generateRandomPlaintext(INITIALFILENAME, fileSize);
		TestTimer testTimer = new TestTimer();
		
		//Generate Random key
		algorithmInstance.generateRandomKeySet();
		
		Long startTime = System.nanoTime();
		//Encrypts the file
		File encryptedFile = algorithmInstance.encryptFile(fileToEncrypt);
		testTimer.setEncTime(System.nanoTime() - startTime);

		//Decrypts the file
		File decryptedFile = algorithmInstance.decryptFile(encryptedFile);
		testTimer.setDecTime(System.nanoTime() - startTime - testTimer.getEncTime());
		
		DataInputStream originalFileStream;
		DataInputStream modFileStream;
		
		try {
			originalFileStream = new DataInputStream(new FileInputStream(fileToEncrypt));
			modFileStream = new DataInputStream(new FileInputStream(decryptedFile));
			
			System.out.println("Checking file consistency");
			for(int i = 0; i < fileSize ; i++){
				if(originalFileStream.readByte() != modFileStream.readByte()){
					System.out.println("There was an error with file encryption/decryption \n"+
										"The file is not consistance.");
					return null;
				}

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	
		return testTimer;
		
		
	}
	
	private File generateRandomPlaintext(String name, int length){
		int byteLength = length;
		byte[] pt = new byte[byteLength];
		Random rand = new Random();
		File tempFile = new File(dirName + "\\" + name);
		FileOutputStream fileStream = null;
	
		try {
			fileStream = new FileOutputStream(tempFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		rand.nextBytes(pt);
		
		try {
			fileStream.write(pt);
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tempFile;
		
	}

	

}

