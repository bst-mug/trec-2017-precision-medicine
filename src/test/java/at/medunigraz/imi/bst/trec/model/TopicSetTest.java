package at.medunigraz.imi.bst.trec.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Test;

public class TopicSetTest {

	@Test
	public void testFromXML() {
		File topicsFile = new File(getClass().getResource("/topics/topics2017.xml").getPath());
		Set<Topic> topics = (new TopicSet(topicsFile)).getTopics();
		
		assertEquals(30, topics.size());
		assertTrue(topics.contains(new Topic().withNumber(30)));
		
		Topic firstTopic = topics.iterator().next();
		assertEquals(1, firstTopic.getNumber());
		assertEquals("Liposarcoma", firstTopic.getDisease());
		assertEquals("CDK4 Amplification", firstTopic.getGene());
		assertEquals("38-year-old male", firstTopic.getDemographic());
		assertEquals("GERD", firstTopic.getOther());
	}

}
