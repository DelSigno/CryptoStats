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

import cryptostats.algorithms.AlgAES128;
import cryptostats.algorithms.AlgSERPENT128;
import cryptostats.algorithms.AlgSIMON128;
import cryptostats.algorithms.AlgSPECK128;
import cryptostats.algorithms.KeySet;
import cryptostats.algorithms.ValidAlgorithm;
import cryptostats.data.AlgSet;
import cryptostats.data.FileAndTimer;
import cryptostats.timeing.TestTimer;
import NIST.MCT;


public class CryptoStatsSym {

	public static final boolean CHECKFORCONSISTENCY = true;
	
	/**
	 * Some variable we may use
	 */
	private String dirName = System.getProperty("user.dir");       // Used for code conversion
	private File destination = null;     // destination directory File object
	private String cipherName = null; // cipher algorithm name, probably wont use
	
	private ValidAlgorithm algorithmInstance;
	private String algName;

	
	//CONSTRUCTOR
	public CryptoStatsSym(AlgSet alg){
		this.algorithmInstance = alg.getAlg();
		this.algName = alg.getName();
	}
	
	public void update(AlgSet alg){
		this.algorithmInstance = alg.getAlg();
		this.algName = alg.getName();
	}
	
	
   
	public TestTimer testFileTime(int fileSize) {

		//Generate random Plaintext
		File fileToEncrypt = generateRandomPlaintext("testfiles" + "\\" + algName + "_testfile", fileSize);
		TestTimer testTimer = new TestTimer();
		
		//Generate Random key
		algorithmInstance.generateRandomKeySet();
		
		//Long startTime = System.nanoTime();
		//Encrypts the file
		File encryptedFile = algorithmInstance.encryptFile(fileToEncrypt).file;
		//testTimer.setEncTime(System.nanoTime() - startTime);

		//Decrypts the file
		FileAndTimer  goodInfo = algorithmInstance.decryptFile(encryptedFile);
		File decryptedFile = goodInfo.file;
		testTimer = goodInfo.testTimer;
		
		
		
		
		
		if ((CHECKFORCONSISTENCY)&&(Math.random() < .01)) {
			//will check files for consistency
			//From this point on we are just checking for consistency between streams
			DataInputStream originalFileStream;
			DataInputStream modFileStream;
			try {
				originalFileStream = new DataInputStream(new FileInputStream(
						fileToEncrypt));
				modFileStream = new DataInputStream(new FileInputStream(
						decryptedFile));

				System.out.println("Checking file consistency");
				for (int i = 0; i < fileSize; i++) {
					if (originalFileStream.readByte() != modFileStream
							.readByte()) {
						System.out
								.println("There was an error with file encryption/decryption \n"
										+ "The file is not consistance.");
						return null;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return testTimer;
		
		
	}
	
	public File generateRandomPlaintext(String name, int length){
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
