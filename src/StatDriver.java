import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cryptostats.algorithms.AlgAES;
import cryptostats.algorithms.AlgSERPENT;
import cryptostats.algorithms.AlgSIMON;
import cryptostats.algorithms.AlgSPECK;
import cryptostats.algorithms.ValidAlgorithm;
import cryptostats.data.AlgSet;
import cryptostats.timeing.TestTimer;


public class StatDriver {
	
	//AES and Serpent work with long files
	//SIMON and SPECK must have 128/8 file size
	private static final int PLAINTEXTLENGTH = (128/8)*100000;//in bytes
	private static final long NANOCONVERTER = 1000000000L;
	private static final double MEANTOL = 50000;//in bytes/sec
	private static final int ALGCOUNT = 4;
	
	

	public static void main(String[] args) {
		
		DateFormat df = new SimpleDateFormat("MMddyy");
		DateFormat tf = new SimpleDateFormat("_K_mm_ss_");
        Date today = Calendar.getInstance().getTime();        
        String reportDate = df.format(today);
        String reportTime = tf.format(today);
        
        String reportTag = "";
		
		AlgSet[] algorithms = {
				
				new AlgSet(new AlgAES(),reportTag + "AlgAES"),
				new AlgSet(new AlgSERPENT(),reportTag + "AlgSERPENT"),
				new AlgSet(new AlgSIMON(),reportTag + "AlgSIMON"),
				new AlgSet(new AlgSPECK(),reportTag + "AlgSPECK")
		};
		
		ArrayList<ArrayList<Double>> data =  new ArrayList<ArrayList<Double>>();
/*{
				new ArrayList<Double>(),
				new ArrayList<Double>(),
				new ArrayList<Double>(),
				new ArrayList<Double>()
		};*/
		
		double[] avgEncDecTime = new double[algorithms.length];

		String dirName = System.getProperty("user.dir");       // Used for code conversion
		int testing = 0;
		double bytespersecond = 0;
		double abpsm[] = {0,0,0,0,0};
		ValidAlgorithm algorithmInstance;
		CryptoStatsSym meta = new CryptoStatsSym(algorithms[0]);
		
		//all testing takes place in meta.testFileTime for algorithmInstance
		for(int i = 0; i < algorithms.length; i++){
			meta.update(algorithms[i]);
			
			//Adds new data
			data.add(new ArrayList<Double>());
			
			
			
			//Gathers data until variation is reduced to a certain tolarance for the last 5 runs
			//Resets our timeing for every algorithm, although it shouldnt matter
			//Also new loop shit
			abpsm = new double[]{0,0,0,0,0};
			testing = 0;
			while(testing != (-1)){
				//testing starts at 1!!
				testing++;
				TestTimer timer = meta.testFileTime(PLAINTEXTLENGTH);
				
				//Takes the total time for encryption then decryption
				data.get(i).add(((double)timer.getTotalTime())/NANOCONVERTER);
				//System.out.println("test time: " + timer.getTotalTime() );
				bytespersecond = (PLAINTEXTLENGTH*16)/(((double)timer.getTotalTime())/NANOCONVERTER);
				
				
				abpsm[4] = ((abpsm[3]*(((double)testing)-1.0)) + bytespersecond)/((double)testing);
				
				
				//System.out.println("abpsm[4]: " + abpsm[4] );
				for(int j = 0; j < 4; j++){
					//System.out.println("j: " + j );
					//System.out.println("abpsm[j]: " + abpsm[j] );
					//Error in last 5 averages
					if(Math.abs(abpsm[4]-abpsm[j]) > MEANTOL){
						abpsm[0]=abpsm[1];
						abpsm[1]=abpsm[2];
						abpsm[2]=abpsm[3];
						abpsm[3]=abpsm[4];
						//System.out.println("breaking for loop" );
						break;
					}else if(j == 3){//All The averages are within spec
						//System.out.println("Testing = -1" );
						testing = -1;
					}else{
						//System.out.println("elsing" );
						continue;
					}
				}	
				
				//Assigns our final average value to the specific cipher
				avgEncDecTime[i] = abpsm[4];
			}
			
				
			//Prints out the data for the specific cipher
		    try {
		    	File tempFile = new File(dirName + "\\" + "data_" + algorithms[i].getName());
			    FileOutputStream fos = new FileOutputStream(tempFile); 
			    PrintWriter pw = new PrintWriter(tempFile);
				for(int d = 0; d < data.get(i).size(); d++){
					//System.out.println("print writer printing");
					pw.printf("%.12f\n" , data.get(i).get(d));
				}
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			    
			
			

			
			/*System.out.println("Encryption time: " + ((double)timer.getEncTime())/NANOCONVERTER + "\n");
			System.out.println("Decryption time: " + ((double)timer.getDecTime())/NANOCONVERTER + "\n");
			System.out.println("Total time: " + ((double)timer.getTotalTime())/NANOCONVERTER + "\n");
			*/
		}
		
		
		//Prints out the data for the specific cipher
	    try {
	    	File tempFile = new File(dirName + "\\" + "data_"+ reportTag + "global");
		    FileOutputStream fos = new FileOutputStream(tempFile); 
		    PrintWriter pw = new PrintWriter(tempFile);


		    //prints data per alg
		    for(int i = 0; i < algorithms.length; i++){
		    	pw.println("-------" + algorithms[i].getName() + "-------");
		    	
		    	pw.print("final average encryption rate (bytes/second) after convergence: ");
		    	pw.printf("%.12f\n" , avgEncDecTime[i]);
		    }
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
	    System.out.println("Complete");

		
	}
	
}
