package byteblocks;

public class ByteOps {

	public static int getLSB(byte target){
		return target & 1;
	}
	
	/**
	 * Returns the 
	 * @param target byte
	 * @param number number in from the least significant bit(the lst is number zero)
	 * @return
	 */
	public static int getBitNumber(byte target, int number){
		return (target << number) & 1;
	}
	
	/**
	 * Returns a new byte with the selected bit of the target set to value
	 * @param target
	 * @return
	 */
	public static byte[] setBitNumber(byte target, int number, boolean value){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
