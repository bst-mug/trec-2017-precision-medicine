package at.medunigraz.imi.bst.trec.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Topic {

	private int number = 0;
	private String disease = "";
	private String gene = "";
	private String demographic = "";
	private String other = "";

	public Topic() {

	}

	/**
	 * Builds a Topic out of a XML file in the format:
	 * 
	 * <pre>
	 * {@code
	 * <topic number="1">
	 *     <disease>Acute lymphoblastic leukemia</disease>
	 *     <gene>ABL1, PTPN11</gene>
	 *     <demographic>12-year-old male</demographic>
	 *     <other>No relevant factors</other>
	 * </topic>
	 * }
	 * </pre>
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static Topic fromXML(File xmlFile) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		Document doc = null;
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Element element = (Element) doc.getElementsByTagName("topic").item(0);

		return fromElement(element);
	}
	
	public static Topic fromElement(Element element) {
		int number = Integer.parseInt(getAttribute(element, "number"));
		String disease = getElement(element, "disease");
		
		// Backwards compatibility
		String gene = "";
		if (hasElement(element, "variant")) {
			gene = getElement(element, "variant");
		} else if (hasElement(element, "gene")) {
			gene = getElement(element, "gene");
		}
		
		String demographic = getElement(element, "demographic");
		String other = getElement(element, "other");

		Topic topic = new Topic().withNumber(number).withDisease(disease).withGene(gene)
				.withDemographic(demographic).withOther(other);

		return topic;
	}

	public Topic withNumber(int number) {
		this.number = number;
		return this;
	}

	public Topic withDisease(String disease) {
		this.disease = disease;
		return this;
	}

	public Topic withGene(String gene) {
		this.gene = gene;
		return this;
	}

	public Topic withDemographic(String demographic) {
		this.demographic = demographic;
		return this;
	}

	public Topic withOther(String other) {
		this.other = other;
		return this;
	}
	
	private static boolean hasElement(Element element, String name) {
		return element.getElementsByTagName(name).getLength() > 0 ? true : false;
	}

	private static String getElement(Element element, String name) {
		return element.getElementsByTagName(name).item(0).getTextContent();
	}

	private static String getAttribute(Element element, String name) {
		return element.getAttribute(name);
	}

	public int getNumber() {
		return number;
	}

	public String getDisease() {
		return disease;
	}

	public String getGene() {
		return gene;
	}

	public String getDemographic() {
		return demographic;
	}

	public int getAge() {
		try {
			return Integer.parseInt(demographic.replaceAll("[^0-9]+", ""));
		}
		catch (Exception e) {
			return -1;
		}
	}

	public String getSex() {
		if (demographic.toLowerCase().contains("female"))
			return "female";
		else
			return "male";
	}

	public String getOther() {
		return other;
	}
	
	public Map<String, String> getAttributes() {
		Map<String, String> ret = new HashMap<>();
		
		// TODO use reflection
		ret.put("number", String.valueOf(number));
		ret.put("disease", disease);
		ret.put("gene", gene);
		ret.put("variant", gene);	// Backwards compatibility
		ret.put("demographic", demographic);
		ret.put("other", other);
		ret.put("sex", getSex());
		ret.put("age", String.valueOf(getAge()));
		
		return ret;
	}

	@Override
	public String toString() {
		return "Topic [number=" + number + ", disease=" + disease + ", gene=" + gene
				+ ", demographic=" + demographic + ", other=" + other + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		return Objects.equals(number, other.number);
	}

}
