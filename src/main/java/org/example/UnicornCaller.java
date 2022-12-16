package org.example;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.ArrayList;
import java.util.List;

public class UnicornCaller {

    public UnicornCaller(){}

    public List<Unicorn> getAll() {
        List<Unicorn> unicorns = new ArrayList<>();
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

        try {
            httpclient = HttpClients.createDefault();
            httpGet = new HttpGet(url);
            response = httpclient.execute(httpGet);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);
                    unicorn = gson.fromJson(reader, SmallBoy.class);
                    // göra om alla länkar i details så det passar in på vårat API istället?
                    // kanske bara skicka vidare hela jsonskiten?
                    // varför packa upp respons och baka ihop det igen?


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
}
