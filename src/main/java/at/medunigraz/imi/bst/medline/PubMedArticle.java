package at.medunigraz.imi.bst.medline;

import java.util.ArrayList;

public class PubMedArticle {

    public String pubMedId;
    public String publicationYear;
    public String publicationMonth;
    public String docTitle;
    public String docAbstract;
    public ArrayList<String> meshTags;

    public PubMedArticle() {
        this.meshTags = new ArrayList<>();
    }

    public PubMedArticle(String pubMedId, String publicationYear, String publicationMonth, String docTitle, String docAbstract) {
        this.pubMedId = pubMedId;
        this.publicationYear = publicationYear;
        this.publicationMonth = publicationMonth;
        this.docTitle = docTitle;
        this.docAbstract = docAbstract;

        this.meshTags = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\nPMID: " + this.pubMedId + "\n" +
               "TITLE: " + this.docTitle + "\n" +
               "DATE: " + this.publicationMonth + " " + this.publicationYear + "\n" +
               "ABSTRACT: " + this.docAbstract + "\n" +
               "MESHTAGS: " + this.meshTags + "\n";
    }
    
	public int getPublicationYear() {
		int ret = 0;
		
		if (publicationYear == null) {
			return ret;
		}

		try {
			ret = Integer.parseInt(publicationYear);
		} catch (NumberFormatException e) {
			return ret;
		}
		
		return ret;
	}
}
