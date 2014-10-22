package cryptostats.timeing;

/**
 * Holds all the data for a full test of an algorithm, things like encryption time
 * decryption time, total time... that is really it for now
 * @author Booty
 *
 */
public class TestTimer {
	
	private long encryptionTime = 0;
	private long decryptionTime = 0;
	
	public TestTimer(){
		
	}
	
	public void setDecTime(Long newTime){
		decryptionTime = newTime;
	}
	
	public void setEncTime(Long newTime){
		encryptionTime = newTime;
	}
	

	
	public long getDecTime(){
		return decryptionTime;
	}
	
	public long getEncTime(){
		return encryptionTime;
	}
	
	public long getTotalTime(){
		return decryptionTime + encryptionTime;
	}

}
