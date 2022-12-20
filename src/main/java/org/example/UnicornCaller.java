package org.example;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;

import java.io.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UnicornCaller {

    public UnicornCaller(){}

    public List<SmallBoy> getAll() {
        List<SmallBoy> unicorns = new ArrayList<>();
        String url = "http://unicorns.idioti.se/";

        Gson gson = new Gson();

        HttpClient httpclient = null;
        HttpGet httpGet = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        SmallBoy unicorn = null;
        Type type = new TypeToken<ArrayList<SmallBoy>>(){}.getType();
        try {
            httpclient = HttpClients.createDefault();
            httpGet = new HttpGet(url);
            httpGet.addHeader("Accept", "application/json");
            response = httpclient.execute(httpGet);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);
                    unicorns = gson.fromJson(reader, type);



                }   catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Jsonfail with unicorn");
                }
            } else {
                System.out.println("unicorns getAll failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return unicorns;
    }

    public Object get(String id) {

    }

    public static void main(String[] args) {
        UnicornCaller u = new UnicornCaller();
        u.getAll();
    }


}
