package at.medunigraz.imi.bst.clinicaltrial;

import at.medunigraz.imi.bst.medline.PubmedXmlHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClinicalTrial {

    public static Pattern INCL_EXCL_PATTERN = Pattern.compile("[Ii]nclusion [Cc]riteria:(.+)[Ee]xclusion [Cc]riteria:(.+)");

    public String id;
    public String title;
    public String summary;
    public Set<String> sex;
    public int minAge;
    public int maxAge;
    public String inclusion;
    public String exclusion;


    public ClinicalTrial(String id, String title, String summary, Set<String> sex, int minAge, int maxAge, String inclusion, String exclusion) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.sex = sex;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.inclusion = inclusion;
        this.exclusion = exclusion;
    }

    public ClinicalTrial() {
    }


    public static ClinicalTrial fromXml(String xmlClinicalTrial){

        ClinicalTrialXmlHandler handler = new ClinicalTrialXmlHandler();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlClinicalTrial, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(handler.getClinicalTrial());

    }
}
