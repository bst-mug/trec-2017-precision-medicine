package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class StaticMapQueryDecoratorTest extends QueryDecoratorTest {
	
	private static final String KEYWORD = "cancer";

	private final File template = new File(getClass().getResource("/templates/match-keyword.json").getFile());

	private static Map<String, String> keymap = new HashMap<>();

	static {
		keymap.put("keyword", KEYWORD);
	}

	public StaticMapQueryDecoratorTest() {
		this.decoratedQuery = new TemplateQueryDecorator(template,
				new StaticMapQueryDecorator(keymap, new ElasticSearchQuery("trec")));
		this.topic = new Topic();
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new TemplateQueryDecorator(template,
				new StaticMapQueryDecorator(keymap, new DummyElasticSearchQuery()));
		
		decoratedQuery.query(topic);
		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", KEYWORD);
		assertEquals(expected, actual);
		
		decoratedQuery.query(topic);
		actual = decoratedQuery.getJSONQuery();
		expected = String.format("{\"match\":{\"title\":\"%s\"}}", KEYWORD);
		assertEquals(expected, actual);
	}

}
