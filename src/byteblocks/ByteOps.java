package byteblocks;

import javax.xml.bind.DatatypeConverter;

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
		int returnbit = (target >> number) & 0x01;
		System.out.println("getBitNumber -> target: " + target);
		System.out.println("getBitNumber -> return bit: " + returnbit);
		return returnbit;
	}
	
	/**
	 * Returns a new byte with the selected bit of the target set to value. The LSB is zero
	 * @param target
	 * @param number position of the target bit
	 * @param value either one or zero to dictate the new value
	 * @return
	 */
	public static byte setBitNumber(byte target, int number, int value){
		byte[] whatever = {target};
		System.out.println("setBitNumber -> target : "+ DatatypeConverter.printHexBinary(whatever) );
		System.out.println("setBitNumber -> value : "+ value);

		if(value == 1){
			byte returnNumber = (byte)((1 << number) | target);
			System.out.println("setBitNumber -> returnNumber : "+ returnNumber);
			return returnNumber;
		}else if(value == 0){
			byte returnNumber = (byte)(~(1 << number) & target);
			System.out.println("setBitNumber -> returnNumber : "+ returnNumber);
			return returnNumber;
		}else{
			return (Byte) null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
