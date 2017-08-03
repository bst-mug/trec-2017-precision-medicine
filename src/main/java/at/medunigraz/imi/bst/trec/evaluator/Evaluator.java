package at.medunigraz.imi.bst.trec.evaluator;

public interface Evaluator {

	public void evaluate();

	public double getNDCG();

	public double getRPrec();
	
	public double getInfAP();
	
	public double getP10();
	
	public double getF();
}
