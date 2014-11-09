package byteblocks;

import javax.xml.bind.DatatypeConverter;

/**
 * Creates a block. A block is made up of arrays in a way such they are [0][1][2]
 * @author Booty
 *
 */



public class Block {

	public static void main(String[] args){
		byte[] theNumber = {(byte) 0x80,0x01};
		char testChar = 255;
		
		Block testBlock = new Block(theNumber);
		String hex;
		
		hex = DatatypeConverter.printHexBinary(testBlock.getBytes());
		System.out.println(hex); 
		
		for(int i = 0; i < 10; i++){
			testBlock.wrapRotateLeft();
			hex = DatatypeConverter.printHexBinary(testBlock.getBytes());
			System.out.println(hex); 
		}
		
		
	
		
	}
	
	private byte[] block;
	private int length;
	
	public Block(int size){
		block = new byte[size];
		this.length = block.length;
	}
	
	public Block(byte[] ibyte){
		block = ibyte;
		this.length = block.length;
	}
	
	public byte[] getBytes(){
		return block;
	}
	/**  <-----------------------------
	 * [:7::6::5::4::3::2::1::0:][..1..][..2..]...[..n..]
	 */
	public void wrapRotateLeft(){
		
		int msb = ByteOps.getBitNumber(block[0], 7);
		//int lsb = ByteOps.getBitNumber(block[length-1], 0);
		
		for(int i = 0; i < length;i++){
			//Shift block to the left
			block[i] = (byte) (block[i]<<1);
			//Set lsb of new byte to msb of next byte
			//checks to see if we are at the final byte
			if (i == length-1){
				ByteOps.setBitNumber(block[i],0,msb);
			} else { 
				//We are just doing the first bytes so we get the msb of the next byte
				ByteOps.setBitNumber(block[i],0,ByteOps.getBitNumber(block[i+1], 7));
			}
			
		}
	}
}
