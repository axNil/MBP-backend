package org.example;

import io.javalin.Javalin;

public class Main {
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