package at.medunigraz.imi.bst.conceptextraction;

import java.io.File;
import java.io.IOException;

import at.medunigraz.imi.bst.lexigram.GraphUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class ConceptExtractionTest {

    String API_KEY_FILE = "src/main/resources/apikey.txt";
    String API_KEY;

    @Before
    public void SetUp() {
        File apiFile = new File(API_KEY_FILE);
        Assume.assumeTrue(apiFile.exists());

        try {
            API_KEY = FileUtils.readFileToString(apiFile, "UTF-8").replace("\n", "");
        } catch (IOException e) {
            System.out.print("Please place your Lexigram API key in the following file: " + API_KEY_FILE);
            e.printStackTrace();
        }
    }

    @Test
    public void preferredTerm() throws UnirestException {
        Assert.assertEquals(GraphUtils.getPreferredTerm("cervical cancer"), "Carcinoma of cervix");
        Assert.assertEquals(GraphUtils.getPreferredTerm("notfoundlabel"), "notfoundlabel");
    }

    @Test
    public void addSynonyms() throws UnirestException {
        Assert.assertThat(GraphUtils.addSynonymsFromBestConceptMatch("cholangiocarcinoma"),
                containsInAnyOrder("cholangiocellular carcinoma",
                        "cholangiocarcinoma of biliary tract",
                        "bile duct carcinoma",
                        "cholangiocarcinoma",
                        "bile duct adenocarcinoma"));
        Assert.assertThat(GraphUtils.addSynonymsFromBestConceptMatch("notfoundlabel"),
                containsInAnyOrder("notfoundlabel"));
    }

    @Ignore
    public void exampleSearchConcepts() throws UnirestException {
        String keyword = "acute%20lymphoblastic%20leukemia";
        String url = "https://api.lexigram.io/v1/lexigraph/search?q="+ keyword;
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("authorization", "Bearer " + API_KEY)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        JSONArray result = body.getJSONArray("array").getJSONObject(0).getJSONArray("conceptSearchHits");

        /*
        prints concepts found from the search of diabetes
        */
        for(int i = 0; i < result.length(); i++) {
            JSONObject item = result.getJSONObject(i);
            JSONObject concept = item.getJSONObject("concept");
            String types = concept.getJSONArray("types").toString() + " ";

            System.out.println("Concept ID: "+ concept.getString("id") +
                    "Concept label: "+ concept.getString("label") +
                    " types:"+ types);
        }
    }

    @Ignore
    public void exampleEntityExtraction() throws UnirestException {
        String url = "https://api.lexigram.io/v1/extract/entities";
        String text = "The patient was given some hydrocodone for control of her pain."+
                "The patient suffers from bulimia and eating disorder, bipolar disorder,"+
                " and severe hypokalemia. She thinks her potassium might again be low.";

        JSONObject data = new JSONObject();

        data.put("text", text);

        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("authorization", "Bearer " + API_KEY)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(data)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        System.out.println(body);
        JSONArray result = body.getJSONArray("array").getJSONObject(0).getJSONArray("matches");

        System.out.print(body);

        //prints the extracted concepts

        for(int i = 0; i < result.length(); i++) {
            JSONObject item = result.getJSONObject(i);
            String types = item.getJSONArray("types").toString() + " ";
            String context = item.getJSONArray("contexts").toString() + " ";

            System.out.println("Concept ID: "+ item.getString("id") +
                    "Concept label: "+ item.getString("label") +
                    " types:"+ types +
                    " context: "+ context);
        }
        for(int i = 0; i < result.length(); i++) {
            JSONObject item = result.getJSONObject(i);
            String types = item.getJSONArray("types").toString() + " ";
            String context = item.getJSONArray("contexts").toString() + " ";

            System.out.println("Concept ID: "+ item.getString("id") +
                    "Concept label: "+ item.getString("label") +
                    " types:"+ types +
                    " context: "+ context);
        }
    }

}
