package at.medunigraz.imi.bst.medline;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class XmlPubMedArticleSet {

    private static void decompressGzipFile(String gzipFile, String newFile) throws Exception {
        try (FileInputStream fis = new FileInputStream(gzipFile)) {
            GZIPInputStream gis = new GZIPInputStream(fis);
            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gis.read(buffer)) != -1)
                    fos.write(buffer, 0, len);
                fos.close();
            }
            gis.close();
            fis.close();
        }
    }

    public static List<PubMedArticle> getPubMedArticlesFromGzippedXml(String gZippedXmlPubmed){
        String TMP_FILE = "tmp-uncompressed";
        List<PubMedArticle> articles = new ArrayList<>();
        try {
            decompressGzipFile(gZippedXmlPubmed, TMP_FILE);
            articles = getPubMedArticlesFromXml(TMP_FILE);
            new File(TMP_FILE).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(articles);
    }


    public static List<PubMedArticle> getPubMedArticlesFromXml(String xmlPubMed){

        PubmedXmlHandler handler = new PubmedXmlHandler();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlPubMed, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(handler.getArticles());
    }
}
