package at.medunigraz.imi.bst.clinicaltrial;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class XmlTrial {

    private Document currentDocument;

    public XmlTrial(String clinicalTrialXmlFile) {
        try {
            File fXmlFile = new File(clinicalTrialXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            this.currentDocument = doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return this.currentDocument.getElementsByTagName("nct_id").item(0).getTextContent();
    }

    public String getTitle() {
        return this.currentDocument.getElementsByTagName("brief_title").item(0).getTextContent();
    }

    public String getSummary() {
        try {
            return this.currentDocument.getElementsByTagName("textblock").item(0).getTextContent();
        }
        catch (Exception e) {
            return "";
        }
    }

    public String getEligibleSex() {
        try {
            return this.currentDocument.getElementsByTagName("gender").item(0).getTextContent();
        }
        catch (Exception e) {
            return "All";
        }
    }

    public String getEligibleMinimumAge() {
        try {
            return this.currentDocument.getElementsByTagName("minimum_age").item(0).getTextContent();
        }
        catch (Exception e) {
            return "0";
        }
    }

    public String getEligibleMaximumAge() {
        try {
            return this.currentDocument.getElementsByTagName("maximum_age").item(0).getTextContent();
        }
        catch (Exception e) {
            return "100";
        }
    }

    public String getInclusionExclusionCriteria() {
        try {
            Element eligibility = (Element) this.currentDocument.getElementsByTagName("eligibility").item(0);
            Element criteria = (Element) eligibility.getElementsByTagName("criteria").item(0);
            return criteria.getElementsByTagName("textblock").item(0).getTextContent();
        }
        catch (Exception e) {
            return "";
        }
    }
}
