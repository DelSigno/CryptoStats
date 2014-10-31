package byteblocks;

/**
 * Creates a block. A block is made up of arrays in a way such they are [0][1][2]
 * @author Booty
 *
 */



public class Block {

	public static void main(String[] args){
		byte thenumber = 1;
		
		int lsb = ByteOps.getLSB(thenumber);
		System.out.println("thenumber is  now: "+thenumber);
		thenumber = ByteOps.setBitNumber(thenumber,3,lsb);
		System.out.println("thenumber is  now: "+thenumber);
	}
	
	private byte[] block;
	private int length;
	
	public Block(int size){
		block = new byte[size];
		this.length = block.length;
	}
	
	public void wrapRotateLeft(){
		int msb = ByteOps.getBitNumber(block[0], 0);
	}
}
