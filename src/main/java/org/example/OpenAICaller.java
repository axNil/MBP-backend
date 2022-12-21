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
import org.example.beans.ImageUrl;
import org.example.beans.UnicornInfo;
import org.example.beans.UnicornNoID;

import java.io.*;

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
        String imgUrl = null;
        UnicornNoID unicorn = new UnicornNoID();

        try {
            UnicornInfo info = gson.fromJson(message, UnicornInfo.class);

            String nameQuery = Utils.createNameQuery(info.spottedWhere);
            name = getText(nameQuery).replace(".", "");

            String descriptionQuery = Utils.createDescriptionQuery(info, name);
            description = getText(descriptionQuery);

            String imageQuery = Utils.createImageQuery(info, name);
            imgUrl = getImage(imageQuery);
            System.out.println(imgUrl);

            unicorn.name = name;
            unicorn.description = description;
            unicorn.image = imgUrl;
            unicorn.reportedBy = info.reportedBy;
            unicorn.spottedWhere = info.spottedWhere;
            unicorn.spottedWhen = info.spottedWhen;

        } catch (Exception e) {
            //TODO: mer specifik catch
            e.printStackTrace();
        }
        return unicorn;
    }

    public static void main(String[] args) {
        OpenAICaller oc = new OpenAICaller();
        long start = System.currentTimeMillis();
        UnicornInfo info = new UnicornInfo("Svart", "Långt, vitt och lekfullt", 66.252263, 19.048576, "Norrbottens län, Sverige", "Sarkastisk och sömnig");
        String name = oc.getText(Utils.createNameQuery(info.spottedWhere));
        String in = Utils.createDescriptionQuery(info, name);
        String desc = oc.getText(in);
        String ur = oc.getImage(Utils.createImageQuery(info, name));
        System.out.println("name: " + name);
        System.out.println("desc: " + desc);
        System.out.println("url: " + ur);
        System.out.println((System.currentTimeMillis()-start)/1000);
    }

    public ImageUrl[] getMultipleImages(String message) {
        HttpClient httpclient = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;
        ImageUrl[] imgUrls;
        int amountOfImages = 2;

        try {
            httpclient = HttpClients.createDefault();
            httpPost = new HttpPost(imageUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
            httpPost.setEntity(new StringEntity(message, "UTF-8"));
            response = httpclient.execute(httpPost);
            status = response.getStatusLine();
            imgUrls = new ImageUrl[amountOfImages];
            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);
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

                }   catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Jsonfail with openai getMultipleImage");
                }
            } else {
                System.out.println("openai getMultipleImage failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getText(String message) {

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
            httpPost.setEntity(new StringEntity(message, "UTF-8"));
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
                System.out.println("openai getname failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    private String getImage(String message) {
        HttpClient httpclient = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        StatusLine status = null;
        HttpEntity entity = null;
        InputStream data = null;
        Reader reader = null;

        try {
            httpclient = HttpClients.createDefault();
            httpPost = new HttpPost(imageUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + APIKey.APIKEY);
            httpPost.setEntity(new StringEntity(message, "UTF-8"));
            response = httpclient.execute(httpPost);
            status = response.getStatusLine();

            if (status.getStatusCode() == 200) {
                entity = response.getEntity();
                data = entity.getContent();

                try {
                    reader = new InputStreamReader(data);
                    JsonReader jr = gson.newJsonReader(reader);
                    jr.beginObject();
                    for (int i = 0; i < 3; i++) {
                        jr.skipValue();
                    }
                    jr.beginArray();
                    jr.beginObject();
                    jr.skipValue();
                    return jr.nextString().trim();

                }   catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Jsonfail with openai getImage");
                }
            } else {
                System.out.println("openai getimage failed");
                System.out.println("statuscode: " + status.getStatusCode());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


}
