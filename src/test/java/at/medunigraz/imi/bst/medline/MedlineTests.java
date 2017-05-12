package at.medunigraz.imi.bst.medline;

import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class MedlineTests {

    //String DATA_FOLDER = "/Users/plopez/Desktop/trec/";
    String DATA_FOLDER = "/Volumes/PabloSSD/trec/medline_xml_all/";

    String LARGE_XML_GZIPPED = DATA_FOLDER + "medline17n0739.xml.gz";
    String LARGE_XML = DATA_FOLDER + "uncompressed/medline17n0739.xml";
    String SAMPLE_SMALL_XML = "src/main/resources/data/medline-sample.xml";

    List<String> SAMPLE_MULTI_XML = Arrays.asList(
                                                DATA_FOLDER + "uncompressed/medline17n0050.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0100.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0189.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0201.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0355.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0492.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0519.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0666.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0739.xml",
                                                DATA_FOLDER + "uncompressed/medline17n0889.xml"
                                    );

    
    @Test
    public void smallUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(SAMPLE_SMALL_XML);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void largeUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(LARGE_XML);

        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void largeCompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(LARGE_XML_GZIPPED);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void indexDoc() throws Exception {

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);



        long startTime = System.currentTimeMillis();


        PubMedArticle article = new PubMedArticle("12345", "this is the title", "this is the abstract");

        IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("title", StringEscapeUtils.escapeJson(article.docTitle))
                        .field("abstract", StringEscapeUtils.escapeJson(article.docAbstract))
                        .field("meshTags", article.meshTags)
                        .endObject()
                )
                .get();

        System.out.print(response);

        client.close();

        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);

        System.out.println("TIME: " + duration/1000 + " secs");
    }

    @Test
    public void index30KdocsGzipped() throws Exception {

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(LARGE_XML_GZIPPED);

        long xmlDuration = (System.currentTimeMillis() - startTime);

        System.out.println("GUNZIP + PARSE XML: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long indexingDuration = indexArticles(pubMedArticles);

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(xmlDuration + indexingDuration) + " seconds");
    }

    @Test
    public void index30KdocsPlain() throws Exception {

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(LARGE_XML);

        long xmlDuration = (System.currentTimeMillis() - startTime);

        System.out.println("PARSE XML: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long indexingDuration = indexArticles(pubMedArticles);

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(xmlDuration + indexingDuration) + " seconds");

    }

    @Test
    public void index30KdocsPlainBulk() throws Exception {

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(LARGE_XML);

        long xmlDuration = (System.currentTimeMillis() - startTime);

        System.out.println("PARSE XML: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long indexingDuration = indexArticlesBulk(pubMedArticles);

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(xmlDuration + indexingDuration) + " seconds");

    }

    @Test
    public void index300KdocsPlain() throws Exception {

        long startTime = System.currentTimeMillis();

        for (String articleXml : SAMPLE_MULTI_XML) {
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(articleXml);
            System.out.println(pubMedArticles);
            indexArticles(pubMedArticles);
        }

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    @Test
    public void index300KdocsPlainBulk() throws Exception {

        long startTime = System.currentTimeMillis();

        for (String articleXml : SAMPLE_MULTI_XML) {
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(articleXml);
            //System.out.println(pubMedArticles);
            indexArticlesBulk(pubMedArticles);
        }

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    @Test
    public void index300KdocsPlainBulkCompressed() throws Exception {

        long startTime = System.currentTimeMillis();

        for (String articleXml : SAMPLE_MULTI_XML) {
            articleXml = articleXml.replace("uncompressed/", "") + ".gz";
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(articleXml);
            //System.out.println(pubMedArticles);
            indexArticlesBulk(pubMedArticles);
        }

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    @Test
    public void indexAllUncompressed() throws Exception {

        long startTime = System.currentTimeMillis();
        System.out.println("START: " + startTime);

        for (int fileNumber = 1; fileNumber <= 889; fileNumber++) {
            String articleXml = String.format(DATA_FOLDER + "uncompressed/medline17n0%03d.xml", fileNumber);
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(articleXml);
            indexArticlesBulk(pubMedArticles);
        }
        System.out.println("END: " + System.currentTimeMillis());

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    private long indexArticles(List<PubMedArticle> pubMedArticles) throws Exception{

        long startTime = System.currentTimeMillis();

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);

        for (PubMedArticle article: pubMedArticles) {
            IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", StringEscapeUtils.escapeJson(article.docTitle))
                            .field("abstract", StringEscapeUtils.escapeJson(article.docAbstract))
                            .field("meshTags", article.meshTags)
                            .endObject()
                    )
                    .get();
        }

        client.close();

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME: " + indexingDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        return indexingDuration;

    }

    private long indexArticlesBulk(List<PubMedArticle> pubMedArticles) throws Exception{

        long startTime = System.currentTimeMillis();

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (PubMedArticle article: pubMedArticles) {

            bulkRequest.add(client.prepareIndex("medline", "medline", article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", StringEscapeUtils.escapeJson(article.docTitle))
                            .field("abstract", StringEscapeUtils.escapeJson(article.docAbstract))
                            .field("meshTags", article.meshTags)
                            .endObject()
                    )
            );
        }

        BulkResponse bulkResponse = bulkRequest.get();

        if (bulkResponse.hasFailures()) {
            System.out.println("Failures!!!!");
        }

        client.close();

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME BULK: " + indexingDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        return indexingDuration;

    }
}
