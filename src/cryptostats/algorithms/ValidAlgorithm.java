package cryptostats.algorithms;

import java.io.File;
import java.security.InvalidKeyException;

import cryptostats.data.FileAndTimer;

public interface ValidAlgorithm {
	
	public void generateRandomKeySet();
	public FileAndTimer encryptFile(File fileIn);
	public FileAndTimer decryptFile(File fileIn);
}
