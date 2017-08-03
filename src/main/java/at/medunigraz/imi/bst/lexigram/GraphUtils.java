package at.medunigraz.imi.bst.lexigram;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class GraphUtils {

    static String API_KEY_FILE = "src/main/resources/apikey.txt";
    static String API_KEY;

    static {
        File apiFile = new File(API_KEY_FILE);

        try {
            API_KEY = FileUtils.readFileToString(apiFile, "UTF-8").replace("\n", "");
        } catch (IOException e) {
            System.out.print("Please place your Lexigram API key in the following file: " + API_KEY_FILE);
            e.printStackTrace();
        }
    }

    /* Retrieves the preferred term ("label") of the best-matching concept.
    *  If no match, it returns itself */
    public static String getPreferredTerm(String label) throws UnirestException {
        String url = "https://api.lexigram.io/v1/lexigraph/search?q="+ URLEncoder.encode(label);

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("authorization", "Bearer " + API_KEY)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        JSONArray results = body.getJSONArray("array").getJSONObject(0).getJSONArray("conceptSearchHits");

        try {
            if (results.length() == 0)
                return label;
            JSONObject item = results.getJSONObject(0);
            JSONObject concept = item.getJSONObject("concept");
            String conceptId = concept.getString("id");

            /* Get info (label and synonyms) of concept */

            url = "https://api.lexigram.io/v1/lexigraph/concepts/"+ conceptId;
            response = Unirest.get(url)
                    .header("authorization", "Bearer " + API_KEY)
                    .asJson();
            body = new JSONObject(response.getBody()).getJSONArray("array").getJSONObject(0);

            return cleanUpString(body.getString("label"));

        }
        catch (Exception e) {
            throw e;
        }

        //return label;
    }

    /* Possibly the most useful function: Given a string, it searches for the best-matching concept and adds all synonyms
     *  If no match, it returns a list with itself */
    public static List<String> addSynonymsFromBestConceptMatch(String label) throws UnirestException {
        String url = "https://api.lexigram.io/v1/lexigraph/search?q=" + URLEncoder.encode(label);

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("authorization", "Bearer " + API_KEY)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        JSONArray results = body.getJSONArray("array").getJSONObject(0).getJSONArray("conceptSearchHits");

        try {
            List<String> keywordAndSynonyms = new ArrayList<>();
            keywordAndSynonyms.add(label);

            /* Get first concept (most relevant search hit) */
            if (results.length() == 0)
                return Collections.singletonList(label);
            JSONObject item = results.getJSONObject(0);
            JSONObject concept = item.getJSONObject("concept");
            String conceptId = concept.getString("id");

            /* Get info (label and synonyms) of concept */

            url = "https://api.lexigram.io/v1/lexigraph/concepts/"+ conceptId;
            response = Unirest.get(url)
                    .header("authorization", "Bearer " + API_KEY)
                    .asJson();
            body = new JSONObject(response.getBody()).getJSONArray("array").getJSONObject(0);

            keywordAndSynonyms.add(body.getString("label"));

            JSONArray synonymsJson = body.getJSONArray("synonyms");

            for(int i = 0; i < synonymsJson.length(); i++) {
                keywordAndSynonyms.add(synonymsJson.get(i).toString());
            }

            return cleanUpList(keywordAndSynonyms);
        }
        catch (Exception e) {
            throw e;
        }

        //return Collections.singletonList(label);
    }

    /* TODO: Not really useful,to be removed */
    /*
    public static List<String> addAllMatchingConceptMainLabels(String label) throws UnirestException {

        String url = "https://api.lexigram.io/v1/lexigraph/search?q=" + URLEncoder.encode(label);

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("authorization", "Bearer " + API_KEY)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        JSONArray results = body.getJSONArray("array").getJSONObject(0).getJSONArray("conceptSearchHits");

        List<String> labelAndSynonyms = new ArrayList<>();

        labelAndSynonyms.add(label);

        for(int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            JSONObject concept = item.getJSONObject("concept");
            labelAndSynonyms.add(concept.getString("label"));
        }
        return cleanUpList(labelAndSynonyms);
    }
    */

    private static List<String> cleanUpList(List<String> labels) {
        Set<String> cleanLabels = new HashSet<>();
        cleanLabels.addAll(labels.stream().map(String::toLowerCase).map(l -> l.replaceAll("\\(.*\\)", "")).collect(Collectors.toList()));
        cleanLabels = cleanLabels.stream().map(l -> l.replaceAll("\\[.*\\]", "")).collect(Collectors.toSet());
        cleanLabels = cleanLabels.stream().map(l -> l.split(",")[0]).collect(Collectors.toSet());
        List<String> cleanLabelsArrayList = new ArrayList<>();
        cleanLabelsArrayList.addAll(cleanLabels);
        return cleanLabelsArrayList;
    }

    private static String cleanUpString(String label) {
        String cleanLabel;
        cleanLabel = label.toLowerCase();
        cleanLabel = label.replaceAll("\\(.*\\)", "");
        cleanLabel = cleanLabel.replaceAll("\\[.*\\]", "");
        cleanLabel = cleanLabel.split(",")[0];
        return cleanLabel;
    }
}
