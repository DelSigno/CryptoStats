package byteblocks;

public class ByteOps {

	/*public static void main(String[] args){
		byte thenumber = 1;
		
		int lsb = getLSB(thenumber);
		System.out.println("thenumber is  now: "+thenumber);
		thenumber = setBitNumber(thenumber,3,lsb);
		System.out.println("thenumber is  now: "+thenumber);
	}*/
	
	
	
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
	 * Returns a new byte with the selected bit of the target set to value. The LSB is zero
	 * @param target
	 * @return
	 */
	public static byte setBitNumber(byte target, int number, int value){
		if(value == 1){
			return (byte)((1 << number) | target);
		}else if(value == 0){
			return (byte)(~(1 << number) & target);
		}else{
			return (Byte) null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
