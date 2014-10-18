package cryptostats.algorithms;

import java.io.File;
import java.security.InvalidKeyException;

public interface ValidAlgorithm {
	
	public KeySet generateRandomKeySet();
	public File encryptFile(File fileIn, Object key);
	public File decryptFile(File fileIn, Object key);
}
