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

    /**
     * Calls the Unicorn caller and converts the urls to ours. Marshalls the list of objects to JSON.
     * @param ctx Context object connected to the caller.
     */
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

    /**
     * Fetches a Unicorn object from the Unicorn caller and marshalls it to JSON and returns it to the caller
     * @param ctx Context object containing an id as its path parameter.
     */
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

    /**
     * Calls the Unicorn caller and posts a unicorn to Johans database.
     * @param ctx Context object containing all the fields in the UnicornNoId class in its body.
     */
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

    /**
     * Calls the OpenAI caller and returns a unicorn without id through the Context object
     * @param ctx Context object containing the attributes in the UnicornInfo class in its body.
     */
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

    /**
     * Calls the Unicorn Caller to fetch a Unicorn from Johans database and uses it to call the OpenAPI caller
     * that generates an array of urls that is returned to the caller through the Context object
     * @param ctx Context object containing an id as its path parameter.
     */
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
