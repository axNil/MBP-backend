package org.example;



import apikey.APIKey;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class APIRunner {
    public static void main(String[] args) {

        /*
        String url = "https://api.openai.com/v1/completions";
        CloseableHttpClient httpclient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        Gson json = new Gson();
        try {
            // Create the client that will call the API
            httpclient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Accept", "");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer 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");
            httpPost.setEntity(new StringEntity("{\n" +
                    "  \"model\": \"text-davinci-003\",\n" +
                    "  \"prompt\": \"Give me 10 names for a project about unicorn data\",\n" +
                    "  \"max_tokens\": 500,\n" +
                    "  \"temperature\": 1\n" +
                    "}"));

            // Call the API and verify that all went well
            response = httpclient.execute(httpPost);
            status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                // All went well. Let's fetch the data
                entity = response.getEntity();
                data = entity.getContent();
                String s = new String(data.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(s);

                //json.toJson(data);
               // System.out.println(json.toString());
            } else {
                // Something didn't went well. No calls for us.
                System.out.println("SR didn't respond in a good manner.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
        String s = """
                {
                  "model": "text-ada-001",
                  "prompt": "Give me 10 names for a project about unicorn data",
                  "max_tokens": 500,
                  "temperature": 1
                }""";
        try {
            HttpResponse<JsonNode> response;
            JsonNode jn = new JsonNode(s);
            String url = "https://api.openai.com/v1/completions";
            Map<String, String> map = new HashMap<>();
            map.put("Authorization", APIKey.apiKey);
            map.put("Content-Type", "application/json");
            response = Unirest.post(url)
                    .headers(map)
                    .body(s)
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


    }
}
