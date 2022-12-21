package org.example;

import io.javalin.http.Context;
import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;

import java.util.List;


public class APIRunner {
    private UnicornCaller uc;
    private OpenAICaller oc;

    public APIRunner() {
        oc = new OpenAICaller();
        uc = new UnicornCaller();
    }


    public void getAllUnicorns(Context ctx) {
        List<SmallBoy> smallBoys = uc.getAll();
        for (SmallBoy s : smallBoys) {
            s.details = "http://localhost:5008/v1/unicorns/" + s.id;
        }
        ctx.json(smallBoys);
    }

    public void getUnicorn(Context ctx) {
        ctx.json(uc.get(ctx.pathParam("id")));
    }

    public void postUnicorn(Context ctx) {
        uc.post(ctx.body());
    }

    public void searchUnicorn(Context ctx) {
        ctx.json(oc.searchUnicorn(ctx.body()));
    }

    public void getMorePictures(Context ctx) {
        Unicorn unicorn = uc.get(ctx.pathParam("id"));

        ctx.json(oc.getMultipleImages(Utils.createMultiImageQuery(unicorn)));
    }
}
