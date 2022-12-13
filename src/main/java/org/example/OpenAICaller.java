package org.example;

import apikey.APIKey;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenAICaller {
    Map<String, String> map;

    public OpenAICaller() {
        map = new HashMap<>();
        map.put("Authorization", APIKey.apiKey);
        map.put("Content-Type", "application/json");
    }
    public String getDescription(String message) {
        HttpResponse<JsonNode> response;
        try {
            JsonNode jn = new JsonNode(message);
            String url = "https://api.openai.com/v1/completions";

            response = Unirest.post(url)
                    .headers(map)
                    .body(jn)
                    .asJson();

            System.out.println(response.getBody());
            System.out.println();
            Unirest.shutdown();
        } catch (UnirestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "response.getBody;";
    }

    public String getImage() {
        return "";
    }
}
