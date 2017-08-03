package at.medunigraz.imi.bst.medline;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
    USEFUL links:
        https://www.mkyong.com/java/how-to-read-xml-file-in-java-sax-parser/
        http://www.journaldev.com/1198/java-sax-parser-example
        http://stackoverflow.com/questions/7209946/using-sax-parser-how-do-you-parse-an-xml-file-which-has-same-name-tags-but-in-d
 */
public class PubmedXmlHandler extends DefaultHandler {

    private static final String TAG_PUBMED_ARTICLE_SET = "PubmedArticleSet";
    private static final String TAG_PUBMED_ARTICLE = "PubmedArticle";
    private static final String TAG_PUBMED_ID = "PMID";
    private static final String TAG_MEDLINE_CITATION = "MedlineCitation";
    private static final String TAG_ARTICLE = "Article";
    private static final String TAG_ARTICLE_TITLE = "ArticleTitle";
    private static final String TAG_ABSTRACT = "Abstract";
    private static final String TAG_ABSTRACT_TEXT = "AbstractText";

    private final Stack<String> tagsStack = new Stack<String>();
    private final StringBuilder tempVal = new StringBuilder();

    private List<PubMedArticle> articles;
    private PubMedArticle article;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        pushTag(qName);
        tempVal.setLength(0);
        if (TAG_PUBMED_ARTICLE_SET.equalsIgnoreCase(qName)) {
            articles = new ArrayList<>();
        } else if (TAG_PUBMED_ARTICLE.equalsIgnoreCase(qName)) {
            article = new PubMedArticle();
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        String tag = peekTag();
        if (!qName.equals(tag)) {
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();

        if (tag.equalsIgnoreCase(TAG_PUBMED_ID) &&
            parentTag.equalsIgnoreCase(TAG_MEDLINE_CITATION)) {
            article.pubMedId = tempVal.toString().trim();
        }

        if (tag.equalsIgnoreCase(TAG_ARTICLE_TITLE) &&
            parentTag.equalsIgnoreCase(TAG_ARTICLE)) {
            article.docTitle = tempVal.toString().trim();
        }

        if (tag.equalsIgnoreCase(TAG_ABSTRACT_TEXT) &&
            parentTag.equalsIgnoreCase(TAG_ABSTRACT)) {
            article.docAbstract = tempVal.toString().trim();
        }

        if (tag.equalsIgnoreCase("DescriptorName") &&
            parentTag.equalsIgnoreCase("MeshHeading")) {
            article.meshTags.add(tempVal.toString().trim());
        }

        if (tag.equalsIgnoreCase("Year") &&
                parentTag.equalsIgnoreCase("PubDate")) {
            article.publicationYear = tempVal.toString().trim();
        }

        if (tag.equalsIgnoreCase("Month") &&
                parentTag.equalsIgnoreCase("PubDate")) {
            article.publicationMonth = tempVal.toString().trim();
        }


        if (TAG_PUBMED_ARTICLE.equalsIgnoreCase(tag)) {
            articles.add(article);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        tempVal.append(ch, start, length);
    }

    public void startDocument() {
        pushTag("");
    }

    public List<PubMedArticle> getArticles() {
        return articles;
    }

    private void pushTag(String tag) {
        tagsStack.push(tag);
    }

    private String popTag() {
        return tagsStack.pop();
    }

    private String peekTag() {
        return tagsStack.peek();
    }
}
