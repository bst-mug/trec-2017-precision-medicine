package at.medunigraz.imi.bst.conceptextraction;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ConceptExtractionTest {

    String API_KEY_FILE = "src/main/resources/apikey.txt";
    String API_KEY;

    public ConceptExtractionTest(){
        try {
            API_KEY = FileUtils.readFileToString(new File(API_KEY_FILE), "UTF-8");
        } catch (IOException e) {
            System.out.print("Please place your Lexigram API key in the following file: src/main/resources/apikey.txt");
            e.printStackTrace();
        }
    }

    @Test
    public void exampleEntityExtraction() throws UnirestException {
        String url = "https://api.lexigram.io/v1/extract/entities";
        String text = "The patient was given some hydrocodone for control of her pain."+
                "The patient suffers from bulimia and eating disorder, bipolar disorder,"+
                " and severe hypokalemia. She thinks her potassium might again be low.";

        JSONObject data= new JSONObject();
        data.put("text", text);
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("authorization", "Bearer " + API_KEY)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(data)
                .asJson();
        JSONObject body = new JSONObject(response.getBody());
        JSONArray result = body.getJSONArray("array").getJSONObject(0).getJSONArray("matches");

        /*
        prints the extracted concepts
        */
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
