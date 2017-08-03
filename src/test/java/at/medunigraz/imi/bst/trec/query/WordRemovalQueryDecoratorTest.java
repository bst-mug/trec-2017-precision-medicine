package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class WordRemovalQueryDecoratorTest extends QueryDecoratorTest {
	private static final String DISEASE = "Thyroid cancer";
	private static final String FILTERED_DISEASE = "Thyroid";

	private final File template = new File(getClass().getResource("/templates/match-title.json").getFile());

	public WordRemovalQueryDecoratorTest() {
		this.decoratedQuery = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(template, new ElasticSearchQuery("trec")));
		this.topic = new Topic().withDisease(DISEASE);
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(template, new DummyElasticSearchQuery()));
		decoratedQuery.query(topic);

		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", FILTERED_DISEASE);
		assertEquals(expected, actual);
	}
}
