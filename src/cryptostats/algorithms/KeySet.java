package cryptostats.algorithms;

public class KeySet {
	
	private final Object encryptionKey;
	private final Object decryptionKey;
	
	public KeySet(Object encryptionKey, Object decryptionKey){
		this.encryptionKey = encryptionKey;
		this.decryptionKey = decryptionKey;
	}
	
	public Object getEncKey(){
		return encryptionKey;
	}
	
	public Object getDecKey(){
		return decryptionKey;
	}

}
