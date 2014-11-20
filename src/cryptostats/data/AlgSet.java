package cryptostats.data;

import cryptostats.algorithms.ValidAlgorithm;

public class AlgSet {
	private ValidAlgorithm alg;
	private String name;
	
	public AlgSet(ValidAlgorithm nAlg, String nName){
		this.alg = nAlg;
		this.name = nName;
	}
	
	public ValidAlgorithm getAlg(){
		return alg;
	}
	public String getName(){
		return name;
	}

}
