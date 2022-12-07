package org.example;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class APIRunner {
    public static void main(String[] args) {
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.get("http://unicorns.")
                    .queryString("format", "json")
                    .asJson();

            System.out.println("Response from SR:");
            System.out.println(response.getBody());
            System.out.println();

            JsonNode json = response.getBody();
            JSONObject envelope = json.getObject();
            JSONObject channel = envelope.getJSONObject("channel");

            System.out.println("Channel name: " + channel.getString("name"));
            System.out.println("Channel id: " + channel.getInt("id"));

            JSONObject stream = channel.getJSONObject("liveaudio");
            System.out.println("Stream URL: " + stream.getString("url"));

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
