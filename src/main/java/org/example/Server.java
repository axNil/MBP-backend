package org.example;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class Server {
    public static void main(String[] args) {
        APIRunner runner = new APIRunner();
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(CorsPluginConfig::anyHost);
            });
        }).start(5008);

        //Get all unicorns from database
        app.get("v1/unicorns/", (ctx) -> {
            System.out.println("Get all unicorns was performed");
            runner.getAllUnicorns(ctx);
        });

        //Get a single unicorn from id
        app.get("v1/unicorns/{id}", (ctx) -> {
            System.out.println("Get single unicorn was performed");
            runner.getUnicorn(ctx);
        });

        app.get("v1/unicorns/pictures/{id}", (ctx) -> {
            System.out.println("Get more pictures was performed");
            runner.getMorePictures(ctx);
        });

        //Add unicorn to database
        app.post("v1/unicorns/", (ctx) -> {
            System.out.println("Add unicorn to database was performed");
            runner.postUnicorn(ctx);
        });

        //Acquire new unicorn from openAI
        app.post("v1/unicorns/search", (ctx) -> {
            System.out.println("Unicorn search was performed");
            runner.searchUnicorn(ctx);
        });

    }
}