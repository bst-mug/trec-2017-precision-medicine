package at.medunigraz.imi.bst.trec.evaluator;

public abstract class AbstractEvaluator implements Evaluator {
	private double ndcg = 0;
	private double rprec = 0;
	private double infap = 0;
	private double p10 = 0;
	private double f = 0;

	@Override
	public double getNDCG() {
		if (ndcg == 0) {
			evaluate();
		}
		return ndcg;
	}

	@Override
	public double getRPrec() {
		if (rprec == 0) {
			evaluate();
		}
		return rprec;
	}

	@Override
	public double getInfAP() {
		if (infap == 0) {
			evaluate();
		}
		return infap;
	}

	@Override
	public double getP10() {
		if (p10 == 0) {
			evaluate();
		}
		return p10;
	}

	@Override
	public double getF() {
		if (f == 0) {
			evaluate();
		}
		return f;
	}

}
