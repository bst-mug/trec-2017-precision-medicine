package at.medunigraz.imi.bst.clinicaltrial;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.search.ElasticClientFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Indexing {

    public static void main(String[] args) throws Exception {
        if (args.length == 1)
            indexAllClinicalTrials(args[0]);
        else
            System.out.println("1 arg needed: folder containing the clinical trials");
    }

    static long indexAllClinicalTrials(String dataFolderWithFiles) throws Exception {

        List<ClinicalTrial> clinicalTrials = getClinicalTrialsFromFolder(dataFolderWithFiles);

        System.out.println("CLINICALTRIALS TOTAL READ: " + clinicalTrials.size());
        System.out.println("STARTING INDEXING");

        Client client = ElasticClientFactory.getClient();

        long startTime = System.currentTimeMillis();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (ClinicalTrial trial: clinicalTrials) {

            System.out.println("ADDING: " + trial.id);

            bulkRequest.add(client.prepareIndex(TrecConfig.INDEX_TRIALS_NAME, TrecConfig.TRIALS_TYPE, trial.id)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("id", trial.id)
                            .field("title", StringEscapeUtils.escapeJson(trial.title))
                            .field("summary", StringEscapeUtils.escapeJson(trial.summary))
                            .field("sex", trial.sex)
                            .field("minimum_age", trial.minAge)
                            .field("maximum_age", trial.maxAge)
                            .field("inclusion", StringEscapeUtils.escapeJson(trial.inclusion))
                            .field("exclusion", StringEscapeUtils.escapeJson(trial.exclusion))
                            .endObject()
                    )
            );
        }

        BulkResponse bulkResponse = bulkRequest.get();

        if (bulkResponse.hasFailures()) {
            System.out.println("Failures!!!!");
        }

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME BULK: " + indexingDuration/1000 + " secs - " + clinicalTrials.size() + " articles");

        return indexingDuration;


    }

    public static List<ClinicalTrial> getClinicalTrialsFromFolder(String dataFolderWithFiles) throws Exception {

        System.out.println("DATA FOLDER: " + dataFolderWithFiles);

        List<ClinicalTrial> clinicalTrials = new ArrayList<>();

        File folder = new File(dataFolderWithFiles);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                clinicalTrials.add(getClinicalTrialFromFile(listOfFiles[i].getAbsolutePath()));
            }
        }

        return(clinicalTrials);
    }

    public static ClinicalTrial getClinicalTrialFromFile(String xmlTrialFileName) throws IOException {

        return(ClinicalTrial.fromXml(xmlTrialFileName));
    }
}
