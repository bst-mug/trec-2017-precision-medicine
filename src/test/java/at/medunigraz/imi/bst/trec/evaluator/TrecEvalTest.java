package at.medunigraz.imi.bst.trec.evaluator;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TrecEvalTest {

	private static final String GOLD = "/gold-standard/test.qrels";
	private static final String RESULTS = "/results/test.trec_results";

	@Test
	public void testMissingFiles() {
		// FIXME
	}
	
	@Test
	public void testEvaluate() {
		File goldStandard = new File(getClass().getResource(GOLD).getFile());
		File results = new File(getClass().getResource(RESULTS).getFile());

		TrecEval t = new TrecEval(goldStandard, results);
		assertEquals(0.6309, t.getNDCG(), 0.00001);
		assertEquals(0.0, t.getRPrec(), 0.00001);	// TODO Figure out a better test set
		assertEquals(0.5, t.getInfAP(), 0.00001);
		assertEquals(0.1, t.getP10(), 0.00001);
		assertEquals(0.5, t.getF(), 0.00001);
	}
}
