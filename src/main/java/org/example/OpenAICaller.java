package org.example;

import apikey.APIKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.example.beans.Unicorn;
import org.example.beans.UnicornInfo;
import org.example.beans.UnicornNoID;

import java.io.*;

import java.util.Map;

public class OpenAICaller {
    private final String textUrl;
    private final String imageUrl;
    private final Gson gson;

    public OpenAICaller() {
        textUrl = "https://api.openai.com/v1/completions";
        imageUrl = "https://api.openai.com/v1/images/generations";
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }
    public UnicornNoID searchUnicorn(String message) {
        String name = null;
        String description = null;

        try {
            UnicornInfo info = gson.fromJson(message, UnicornInfo.class);
            name = getName(Utils.createNameQuery(info.spottedWhere.lon, info.spottedWhere.lat));
            description = getDescription(Utils.createDescriptionQuery(info));
        } catch (Exception e) {
            //TODO: mer specifik catch
            e.printStackTrace();
        }
        return null;
    }

    private String getDescription(String message) {
        return "";
    }

    public static void main(String[] args) {
        OpenAICaller oc = new OpenAICaller();
        String s  = oc.getName(Utils.createNameQuery(50, 50));
        System.out.println(s);
    }
    private String getName(String message) {

        HttpClient httpclient = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        try {
            httpclient = HttpClients.createDefault();
            httpPost = new HttpPost(textUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
            httpPost.setEntity(new StringEntity(message));
            response = httpclient.execute(httpPost);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);
                    JsonReader jr = gson.newJsonReader(reader);
                    jr.beginObject();
                    for (int i = 0; i < 9; i++) {
                        jr.skipValue();
                    }
                    jr.beginArray();
                    jr.beginObject();
                    jr.skipValue();
                    return jr.nextString().trim();

                }   catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Jsonfail with openai getname");
                }
            } else {
                System.out.println("unicorns get failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    private String getImage(String message) {
        return "";
    }
}
