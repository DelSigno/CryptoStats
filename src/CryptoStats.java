import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.StringTokenizer;

import cryptostats.algorithms.AlgAES;
import cryptostats.algorithms.KeySet;
import cryptostats.algorithms.ValidAlgorithm;
import NIST.MCT;


public class CryptoStats {

	/**
	 * We don't like arguments, its harder then statics, not sure why I even
	 * bothered implementing, I'm a douche
	 */
	private static final String ALGNAME = "AlgAES";
	ValidAlgorithm algorithmInstance = new AlgAES();
	private static final int PLAINTEXTLENGTH = 1000;//in kilobytes
	
	
	
	
	/**
	 * pretty much completely ripped off NIST test thing. I'm pretty sure it's cool 
	 * because I ain't gettin' paid for any of dis shit. Anyways, mad props to Raif S. Naffah, You da real MVP
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(
	            "Yo dawg, I hear you like tests\n" +
	            "So we made test it test so you can test while you test\n" +
	            "Mad props to Cryptix Dev Team, keep up the good fight\n" +
	            "#YOLOSWAG \n\n");
	        CryptoStats cmd = new CryptoStats();
	        cmd.processOptions(args);
	        cmd.run();

	}
	
	/**
	 * Some variable we may use
	 */
	private String dirName = null;       // Used for code conversion
	private File destination = null;     // destination directory File object
	private String cipherName = null; // cipher algorithm name, probably wont use
	private int[] keys = new int[] {256}; // key-length values to test with
	private String keyLengths = "256";


	//HOLY SHIT YOU CAN STORE METHODS, I never realized you could do this.
	//For real, this is crazy, my mind is blown. Like, greatest day ever...
	Method genKeySet = null; // reference to generateRandomKeySet
    Method encrypt = null; // reference to encryptFile
    Method decrypt = null; // reference to decryptFile
    
	private void processOptions(String[] args) {
        //Gets the total number of arguments
		int argc = args.length;
       
		//Preps variables for use in loop
        int i = -1;
        String cmd = "";
        boolean next = true;
        
        //Begins looping though arguments until no more arguments
        while (true) {
            if (next) {
                i++;
                if (i >= argc)
                    break;
                else
                    cmd = args[i];
            } else {
                cmd = "-" + cmd.substring(2);
            }
            
            if (cmd.startsWith("-z")) {              // Activate zetta mode
                System.out.println("Secret zetta mode activated");
                next = (cmd.length() == 2);
            } else if (cmd.startsWith("-t")) {       // Name your turtle
                String turtleName = args[i + 1];
                System.out.println("Your turtles name is: " + turtleName);
                i++;
                next = true;
            } else // it's the cipher
                cipherName = cmd;
        }
        // sanity checks
        if (cipherName == null)
            //halt("Missing cipher algorithm name");
        	//No algorithm giving so getting static
        	cipherName = ALGNAME;
        
        //No need for error handling here
        /*if (cipherName.length() > 1 && (cipherName.startsWith("\"") || cipherName.startsWith("'")))
            cipherName = cipherName.substring(2, cipherName.length() - 2);
         */
        
        
        
        //Pretty much just sets director to user.dir, we dont care about custom dirs
        if (dirName == null)
            dirName = System.getProperty("user.dir");
        destination = new File(dirName);
        if (! destination.isDirectory())
            halt("Destination <" + destination.getName() +
                "> is not a directory");
        
        //This is when things get fun
        String algoritm = "cryptostats.algorithms." + cipherName;
        try {
            Class algorithm = Class.forName(algoritm);
            // inspect the Basic API class
            Method[] methods = algorithm.getDeclaredMethods();
            for (i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                int params = methods[i].getParameterTypes().length;
                if (name.equals("generateRandomKeySet") && (params == 0))
                    genKeySet = methods[i];
                else if (name.equals("encryptFile") && (params == 2))
                    encrypt = methods[i];
                else if (name.equals("decryptFile") && (params == 2))
                    decrypt = methods[i];
            }
            if (genKeySet == null)
                throw new NoSuchMethodException("generateRandomKeySet()");
            if (encrypt == null)
                throw new NoSuchMethodException("encryptFile()");
            if (decrypt == null)
                throw new NoSuchMethodException("decryptFile()");
        } catch (ClassNotFoundException x1) {
            halt("Unable to find "+algoritm+" class");
        } catch (NoSuchMethodException x2) {
            halt("Unable to find "+algoritm+"."+x2.getMessage()+" method");
        }
    }

    /**
     * Print an error message to System.err and halts execution returning
     * -1 to the JVM.
     *
     * @param s A message to output on System.err
     */
    static void halt (String s) {
        System.err.println("\n*** "+s+"...");
        System.exit(-1);
    }
	private void run() {

		//Generate random Plaintext
		File fileToEncrypt = generateRandomPlaintext("testfile", PLAINTEXTLENGTH);
		
		//Generate Random key
		KeySet keySet = null;
		try {
			keySet = (KeySet) genKeySet.invoke(algorithmInstance);
		} catch (IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//Encrypts the file
		Object[] encArgs = new Object[] {fileToEncrypt, keySet.getEncKey()};
		File cipherText = null;
		try {
			cipherText = (File) encrypt.invoke(algorithmInstance, encArgs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//Decrypts the file
		Object[] decArgs = new Object[] {cipherText, keySet.getDecKey()};
		File plainTextPrime = null;
		try {
			plainTextPrime = (File) decrypt.invoke(algorithmInstance, decArgs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	private File generateRandomPlaintext(String name, int length){
		int byteLength = length*1000;
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
