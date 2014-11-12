package cryptostats.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileGeneratorHelper {

	//Number of bytes
	public static byte[] generateRandomByteArray(int length){
		int byteLength = length;
		byte[] pt = new byte[byteLength];
		Random rand = new Random();
		rand.nextBytes(pt);	
		return pt;
		
	}
}
