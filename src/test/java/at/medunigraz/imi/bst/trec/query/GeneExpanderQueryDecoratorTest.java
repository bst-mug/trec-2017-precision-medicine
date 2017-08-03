package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.model.Topic;

public class GeneExpanderQueryDecoratorTest extends QueryDecoratorTest {
	private static final String GENE = "TP53";

	private final File template = new File(getClass().getResource("/templates/match-title-gene.json").getFile());

	private static final Gene.Field[] EXPAND_TO = { Gene.Field.SYMBOL, Gene.Field.SYNONYMS };

	public GeneExpanderQueryDecoratorTest() {
		this.decoratedQuery = new GeneExpanderQueryDecorator(EXPAND_TO,
				new TemplateQueryDecorator(template, new ElasticSearchQuery("trec")));
		this.topic = new Topic().withGene(GENE);
	}

	@Test
	public void testGetTopic() {
		DummyElasticSearchQuery dummyQuery = new DummyElasticSearchQuery();
		Query decorator = new GeneExpanderQueryDecorator(EXPAND_TO, dummyQuery);

		decorator.query(new Topic().withGene(GENE));
		String actual = dummyQuery.getTopic().getGene();
		String expected = "TRP53 BCC7 LFS1 TP53 P53";
		assertEquals(expected, actual);
	}
}
