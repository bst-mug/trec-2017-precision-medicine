package at.medunigraz.imi.bst.trec.model;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class TopicTest {

	@Test
	public void testFromXML() {
		File all = new File(getClass().getResource("/topics/all.xml").getPath());
		Topic topic = Topic.fromXML(all);

		assertEquals(1, topic.getNumber());
		assertEquals("Acute lymphoblastic leukemia", topic.getDisease());
		assertEquals("ABL1, PTPN11", topic.getGene());
		assertEquals("12-year-old male", topic.getDemographic());
		assertEquals("No relevant factors", topic.getOther());
	}

}
