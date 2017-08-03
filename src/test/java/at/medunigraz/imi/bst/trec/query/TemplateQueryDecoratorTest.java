package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecoratorTest extends QueryDecoratorTest {

	private static final String DISEASE_1 = "thyroid";
	private static final String DISEASE_2 = "breast";

	private final File template = new File(getClass().getResource("/templates/match-title.json").getFile());

	public TemplateQueryDecoratorTest() {
		this.decoratedQuery = new TemplateQueryDecorator(template, new ElasticSearchQuery("trec"));
		this.topic = new Topic().withDisease(DISEASE_1);
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new TemplateQueryDecorator(template, new DummyElasticSearchQuery());
		
		topic.withDisease(DISEASE_1);
		decoratedQuery.query(topic);
		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE_1);
		assertEquals(expected, actual);
		
		topic.withDisease(DISEASE_2);
		decoratedQuery.query(topic);
		actual = decoratedQuery.getJSONQuery();
		expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE_2);
		assertEquals(expected, actual);
	}

}
