package org.example;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;

import java.io.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UnicornCaller {
    private Gson gson;
    String url;

    public UnicornCaller() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        url = "http://unicorns.idioti.se/";
    }

    public List<SmallBoy> getAll() throws IllegalStateException, IOException, JsonSyntaxException, JsonIOException {
        List<SmallBoy> unicorns = new ArrayList<>();

        HttpClient httpclient = null;
        HttpGet httpGet = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        SmallBoy unicorn = null;
        Type type = new TypeToken<ArrayList<SmallBoy>>() {
        }.getType();

        httpclient = HttpClients.createDefault();
        httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "application/json");
        response = httpclient.execute(httpGet);
        status = response.getStatusLine();

        if (status.getStatusCode() == 200) {
            entity = response.getEntity();
            data = entity.getContent();

            reader = new InputStreamReader(data);
            unicorns = gson.fromJson(reader, type);

        } else {
            System.out.println("unicorns getAll failed");
            System.out.println("statuscode: " + status.getStatusCode());
            throw new JsonSyntaxException("Ivalid shit"); //change this text later.
        }


        return unicorns;
    }

    public Unicorn get(String id) {
        Unicorn unicorn = new Unicorn();

        HttpClient httpclient = null;
        HttpGet httpGet = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        try {
            httpclient = HttpClients.createDefault();
            httpGet = new HttpGet(url + id);
            httpGet.addHeader("Accept", "application/json");
            response = httpclient.execute(httpGet);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);

                    unicorn = gson.fromJson(reader, Unicorn.class);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Jsonfail with unicorn");
                }
            } else {
                System.out.println("unicorns get failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return unicorn;
    }

    public void post(String body) {
        HttpClient httpclient = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;
        StringEntity stringEntity = null;
        try {
            httpclient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            stringEntity = new StringEntity(body);
            httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                System.out.println("post went well");
            } else {
                System.out.println("unicorns post failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
