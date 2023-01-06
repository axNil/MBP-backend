package org.example;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import org.example.beans.Error;
import org.example.beans.SmallBoy;
import org.example.beans.Unicorn;

import java.io.IOException;
import java.util.List;

/**
 * Middlehand where requests are being funneled out to different services and where all the error handling
 * is being executed.
 */
public class APIRunner {
    private UnicornCaller uc;
    private OpenAICaller oc;

    public APIRunner() {
        oc = new OpenAICaller();
        uc = new UnicornCaller();
    }

    public void getAllUnicorns(Context ctx) {
        try {
            List<SmallBoy> smallBoys = uc.getAll();
            for (SmallBoy s : smallBoys) {
                s.details = "http://localhost:5008/v1/unicorns/" + s.id;
            }
            ctx.json(smallBoys);
        } catch (IllegalStateException | IOException | JsonIOException e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
        } catch (JsonSyntaxException e) {
            ctx.status(400);
            ctx.json(new Error("Bad request"));
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
            System.out.println("Something weird happened");
            System.out.println(e.getMessage());
        }
    }

    public void getUnicorn(Context ctx) {
        try {
            ctx.json(uc.get(ctx.pathParam("id")));
        } catch (IllegalStateException | IOException | JsonIOException e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
        } catch (JsonSyntaxException e) {
            ctx.status(404);
            ctx.json(new Error("No unicorn with this ID exists"));
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
            System.out.println("Something weird happened");
            System.out.println(e.getMessage());
        }
    }

    public void postUnicorn(Context ctx) {
        try {
            uc.post(ctx.body());
        } catch (IllegalStateException | IOException | JsonIOException e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
        } catch (JsonSyntaxException e) {
            ctx.status(400);
            ctx.json(new Error(e.getMessage()));
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
            System.out.println("Something weird happened");
            System.out.println(e.getMessage());
        }
    }

    public void searchUnicorn(Context ctx) {
        try {
            ctx.json(oc.searchUnicorn(ctx.body()));
        } catch (IllegalStateException | IOException e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
        } catch (JsonSyntaxException e) {
            ctx.status(400);
            ctx.json(new Error(e.getMessage()));
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
            System.out.println("Something weird happened");
            System.out.println(e.getMessage());
        }
    }

    public void getMorePictures(Context ctx) {
        try {
            Unicorn unicorn = uc.get(ctx.pathParam("id"));
            ctx.json(oc.getMultipleImages(Utils.createMultiImageQuery(unicorn)));
        } catch (IllegalStateException | IOException | JsonIOException e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
        } catch (JsonSyntaxException e) {
            ctx.status(404);
            ctx.json(new Error("No unicorn with this ID exists"));
            System.out.println(e.getMessage());
        } catch (Exception e) {
            ctx.status(500);
            ctx.json(new Error("Server no compute"));
            System.out.println("Something weird happened");
            System.out.println(e.getMessage());
        }
    }
}
