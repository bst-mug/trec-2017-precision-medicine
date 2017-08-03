package at.medunigraz.imi.bst.extraabstracts;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.medline.PubMedArticle;
import at.medunigraz.imi.bst.trec.search.ElasticClientFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Indexing {

    private final static Pattern YEAR_PATTERN = Pattern.compile("(\\d{4})");

    public static void main(String[] args) throws Exception {
        if (args.length == 1)
            indexAllExtraAbstracts(args[0]);
        else
            System.out.println("1 arg needed: folder containing the extra abstract TXTs");
    }

    static long indexAllExtraAbstracts(String dataFolderWithFiles) throws Exception {

        List<PubMedArticle> extraAbstracts = getExtraAbstractsFromFolder(dataFolderWithFiles);

        System.out.println("EXTRA ABSTRACTS TOTAL: " + extraAbstracts.size());

        Client client = ElasticClientFactory.getClient();

        long startTime = System.currentTimeMillis();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (PubMedArticle article: extraAbstracts) {

            System.out.println("ADDING: " + article.pubMedId);

            bulkRequest.add(client.prepareIndex(TrecConfig.INDEX_NAME, TrecConfig.EXTRA_TYPE, article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("pubmedId", StringEscapeUtils.escapeJson(article.pubMedId))
                            .field("title", StringEscapeUtils.escapeJson(article.docTitle))
                            .field("publicationDate", StringEscapeUtils.escapeJson(StringEscapeUtils.escapeJson((article.publicationYear))))
                            .field("publicationYear", article.getPublicationYear())
                            .field("abstract", StringEscapeUtils.escapeJson(article.docAbstract))
                            .endObject()
                    )
            );
        }

        BulkResponse bulkResponse = bulkRequest.get();

        if (bulkResponse.hasFailures()) {
            System.out.println("Failures!!!!");
        }

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME BULK: " + indexingDuration/1000 + " secs - " + extraAbstracts.size() + " articles");

        return indexingDuration;


    }

    /* We are reusing the same PubMedArticle class even though they are not, for simplicity */
    public static List<PubMedArticle> getExtraAbstractsFromFolder(String dataFolderWithFiles) throws Exception {

        System.out.println("DATA FOLDER: " + dataFolderWithFiles);

        List<PubMedArticle> extraArticles = new ArrayList<>();

        File folder = new File(dataFolderWithFiles);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                extraArticles.add(getExtraAbstractFromFile(listOfFiles[i].getAbsolutePath()));
            }
        }

        return(extraArticles);
    }

    /* We are reusing the same PubMedArticle class even though they are not, for simplicity */
    public static PubMedArticle getExtraAbstractFromFile(String extraAbstractFileName) throws IOException {

        PubMedArticle extraAbstract = new PubMedArticle();

        //System.out.println("DATA FILE: " + extraAbstractFileName);

        File extraAbstractFile = new File(extraAbstractFileName);

        String extraAbstractString = FileUtils.readFileToString(extraAbstractFile, "UTF-8");

        String lines[] = extraAbstractString.split("\\r?\\n");

        Matcher m = YEAR_PATTERN.matcher(lines[0]);

        String year = "";

        if (m.find()) {
            year = m.group(0);
        }

        String id = FilenameUtils.removeExtension(extraAbstractFile.getName());
        String title = lines[1];
        title = title.substring(7, title.length());
        String theAbstract = "";


        for (int pos = 2; pos < lines.length; pos++) {
            theAbstract = theAbstract + lines[pos];
        }

        theAbstract = theAbstract.trim();

        extraAbstract.pubMedId = id;
        extraAbstract.publicationYear = year;
        extraAbstract.docTitle = title;
        extraAbstract.docAbstract = theAbstract;

        //System.out.println(extraAbstract);

        return(extraAbstract);
    }

}
