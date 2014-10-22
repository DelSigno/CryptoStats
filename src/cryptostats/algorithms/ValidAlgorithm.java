package cryptostats.algorithms;

import java.io.File;
import java.security.InvalidKeyException;

public interface ValidAlgorithm {
	
	public void generateRandomKeySet();
	public File encryptFile(File fileIn);
	public File decryptFile(File fileIn);
}
