package at.medunigraz.imi.bst.trec.model;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TopicSet {

	private static final String TAGNAME = "topic";

	private Set<Topic> topics = new HashSet<Topic>();

	public TopicSet(File xmlFile) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		Document doc = null;
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		NodeList xmlTopics = doc.getElementsByTagName(TAGNAME);

		for (int i = 0; i < xmlTopics.getLength(); i++) {
			Element element = (Element) xmlTopics.item(i);
			Topic t = Topic.fromElement(element);
			topics.add(t);
		}
	}

	public Set<Topic> getTopics() {
		return topics;
	}

}
