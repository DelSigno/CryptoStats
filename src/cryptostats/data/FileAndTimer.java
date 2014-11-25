package cryptostats.data;

import java.io.File;

import cryptostats.timeing.TestTimer;

public class FileAndTimer {
	
	//Yea, you know shit is going down hill
	public File file;
	public TestTimer testTimer;
	
	public FileAndTimer(File nFile, TestTimer nTestTimer){
		this.file = nFile;
		this.testTimer = nTestTimer;
	}
	
	public FileAndTimer() {
		file = null;
		testTimer = null;
	}
	

}
