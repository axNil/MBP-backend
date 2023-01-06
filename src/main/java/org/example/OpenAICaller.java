package org.example;

import apikey.APIKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.example.beans.ImageUrl;
import org.example.beans.UnicornInfo;
import org.example.beans.UnicornNoID;

import java.io.*;

/**
 * Serves as the closest connection to OpenAI.
 * This is where all the requests for OpenAI are being executed.
 */
public class OpenAICaller {
    private final String textUrl;
    private final String imageUrl;
    private final Gson gson;

    public OpenAICaller() {
        textUrl = "https://api.openai.com/v1/completions";
        imageUrl = "https://api.openai.com/v1/images/generations";
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    /**
     * Creates a new unicorn based on the information from the client.
     * A name is first created then it is used in the request for a unicorn description.
     * Lastly it requests a picture based on the description.
     * @param body Information passed in from the client.
     * @return Unicorn without an ID.
     */
    public UnicornNoID searchUnicorn(String body) throws IllegalStateException, IOException, JsonSyntaxException {
        UnicornNoID unicorn = new UnicornNoID();

        UnicornInfo info = gson.fromJson(body, UnicornInfo.class);

        //Validate that all fields exists
        Utils.unicornInfoValidator(info);

        //Create name
        String nameQuery = Utils.createNameQuery(info.spottedWhere);
        String name = getText(nameQuery).replace(".", "");

        //Create description
        String descriptionQuery = Utils.createDescriptionQuery(info, name);
        String description = getText(descriptionQuery);

        //Create image
        String imageQuery = Utils.createImageQuery(info, name);
        String imgUrl = getImage(imageQuery);
//        System.out.println(imgUrl);

        unicorn.name = name;
        unicorn.description = description;
        unicorn.image = imgUrl;
        unicorn.reportedBy = info.reportedBy;
        unicorn.spottedWhere = info.spottedWhere;
        unicorn.spottedWhen = info.spottedWhen;

        return unicorn;
    }

    /**
     * Requests two images based on the description passed in from the client.
     * @param message Constructed request based on information from client.
     * @return Array of image urls
     */
    public ImageUrl[] getMultipleImages(String message) throws IOException {
        ImageUrl[] imgUrls;
        int amountOfImages = 2;

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(imageUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
        httpPost.setEntity(new StringEntity(message, "UTF-8"));
        HttpResponse response = httpclient.execute(httpPost);
        StatusLine status = response.getStatusLine();
        imgUrls = new ImageUrl[amountOfImages];

        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();

            Reader reader = new InputStreamReader(data);
            JsonReader jr = gson.newJsonReader(reader);

            jr.beginObject();
            for (int i = 0; i < 3; i++) {
                jr.skipValue();
            }
            jr.beginArray();
            for (int i = 0; i < amountOfImages; i++) {
                jr.beginObject();
                jr.skipValue();
                imgUrls[i] = new ImageUrl(jr.nextString().trim());
                jr.endObject();
            }

            return imgUrls;
        } else {
            System.out.println("openai getMultipleImage failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new IOException();
        }
    }

    /**
     * Requests text from OpenAI and handles the response.
     * @param message Constructed request message based on information from client.
     * @return Text constructed by OpenAI
     */
    private String getText(String message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(textUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
        httpPost.setEntity(new StringEntity(message, "UTF-8"));
        HttpResponse response = httpclient.execute(httpPost);
        StatusLine status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();
            Reader reader = new InputStreamReader(data);
            JsonReader jr = gson.newJsonReader(reader);

            jr.beginObject();
            for (int i = 0; i < 9; i++) {
                jr.skipValue();
            }
            jr.beginArray();
            jr.beginObject();
            jr.skipValue();

            return jr.nextString().trim();
        } else {
            System.out.println("openai getText failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new IOException();
        }
    }

    /**
     * Requests a single image from Dall-E2.
     * @param message Constructed request message based on description of a unicorn.
     * @return Image url
     */
    private String getImage(String message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(imageUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
        httpPost.setEntity(new StringEntity(message, "UTF-8"));
        HttpResponse response = httpclient.execute(httpPost);
        StatusLine status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();
            Reader reader = new InputStreamReader(data);
            JsonReader jr = gson.newJsonReader(reader);

            jr.beginObject();
            for (int i = 0; i < 3; i++) {
                jr.skipValue();
            }
            jr.beginArray();
            jr.beginObject();
            jr.skipValue();

            return jr.nextString().trim();
        } else {
            System.out.println("openai getimage failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new IOException();
        }
    }
}
