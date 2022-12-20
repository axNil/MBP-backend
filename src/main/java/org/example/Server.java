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

        app.get("v1/unicorns/", (ctx) -> {
            runner.getAllUnicorns(ctx);
            System.out.println("Get all unicorns was performed");
        });

        app.get("v1/unicorns/{id}", (ctx) -> {
            runner.getUnicorn(ctx);
            System.out.println("Get single unicorn was performed");
        });

        app.get("v1/unicorns/pictures/{id}", (ctx) -> {

        });

        app.post("v1/unicorns/", (ctx) -> {
            runner.postUnicorn(ctx);
        });

        app.post("v1/unicorns/search", (ctx) -> {

        });


        /*
        app.get("/", runner::getAll);

        app.get("/{id}", (ctx) -> {
            runner.getId(ctx);
        });

        app.post("/", (ctx) -> {
            runner.post(ctx);
        });

        app.put("/{id}", (ctx) -> {
            runner.putId(ctx);
        });

         */

    }
}