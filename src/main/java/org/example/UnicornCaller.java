package org.example;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;
import org.example.beans.UnicornNoID;

import java.io.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Serves as the closest connection to the unicorn database.
 * This is where all the requests for the database are being executed.
 */
public class UnicornCaller {
    private Gson gson;
    private String url;

    public UnicornCaller() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        url = "http://unicorns.idioti.se/";
    }

    /**
     * Fetches a small amount of information about all the unicorns currently in the database.
     * @return list of unicornobjects with reduced amount of information.
     */
    public List<SmallBoy> getAll() throws IllegalStateException, IOException, JsonSyntaxException, JsonIOException {
        List<SmallBoy> unicorns;
        Type type = new TypeToken<ArrayList<SmallBoy>>() {
        }.getType();

        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        HttpResponse response = httpclient.execute(httpGet);
        StatusLine status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();

            Reader reader = new InputStreamReader(data);
            unicorns = gson.fromJson(reader, type);
            return unicorns;
        } else {
            System.out.println("unicorns getAll failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new IOException();
        }
    }

    /**
     * Fetches all information about a specific unicorn by ID.
     * @param id unicornID.
     * @return Unicornobject containing all the information.
     */
    public Unicorn get(String id) throws IllegalStateException, IOException, JsonSyntaxException, JsonIOException {
        Unicorn unicorn;

        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url + id);
        httpGet.addHeader("Accept", "application/json");
        HttpResponse response = httpclient.execute(httpGet);
        StatusLine status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream data = entity.getContent();
            
            Reader reader = new InputStreamReader(data);
            unicorn = gson.fromJson(reader, Unicorn.class);
            return unicorn;
        } else {
            System.out.println("unicorns get failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new JsonSyntaxException("Invalid request");
        }
    }

    /**
     * Submits a new unicorn to the database.
     * @param body Information about the unicorn passed in by the client.
     */
    public void post(String body) throws IllegalStateException, IOException, JsonSyntaxException, JsonIOException {
        //Validate that all fields exists
        Utils.unicornPostValidator(gson.fromJson(body, UnicornNoID.class));

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPost.setEntity(stringEntity);

        HttpResponse response = httpclient.execute(httpPost);
        StatusLine status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            System.out.println("post went well");
        } else {
            System.out.println("unicorns post failed");
            System.out.println("statuscode: " + status.getStatusCode());
        }
    }
}
