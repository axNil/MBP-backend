package org.example;

import io.javalin.Javalin;

public class Server {
    public static void main(String[] args) {
        APIRunner runner = new APIRunner();
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(5008);

        //Get all unicorns from database
        app.get("v1/unicorns/", (ctx) -> {
            runner.getAllUnicorns(ctx);
            System.out.println("Get all unicorns was performed");
        });

        //Get a single unicorn from id
        app.get("v1/unicorns/{id}", (ctx) -> {
            runner.getUnicorn(ctx);
            System.out.println("Get single unicorn was performed");
        });

        app.get("v1/unicorns/pictures/{id}", (ctx) -> {
            runner.getMorePictures(ctx);
            System.out.println("Get more pictures was performed");
        });

        //Add unicorn to database
        app.post("v1/unicorns/", (ctx) -> {
            runner.postUnicorn(ctx);
            System.out.println("Add unicorn to database was performed");
        });

        //Acquire new unicorn from openAI
        app.post("v1/unicorns/search", (ctx) -> {
            runner.searchUnicorn(ctx);
            System.out.println("Unicorn search was performed");
        });

    }
}