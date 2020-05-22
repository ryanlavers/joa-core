# Joa

A Java web framework inspired by [Koa](https://github.com/koajs/koa) using middleware chaining to define the request processing pipeline

## Examples

Create a server listening on port 8080 that just responds to every request with static text:
```java
Server server = new SunHttpServer(8080);
server.use((ctx, next) -> ctx.response().body("Hello World!"));
server.start();
```

Access request path, parameters, headers, etc., and set the response body, status, and headers using the Context object:
```java
Server server = new SunHttpServer(8080);
server.use((ctx, next) -> {
    if(ctx.request().path().equals("/foo")) {
        ctx.response().header("Content-Type", "text/html; charset=UTF-8");
        // Response status will default to 200 if we set a body
        ctx.response().body("<b>bar<b>");
    }
    else {
        ctx.response().status(404);
        ctx.response().body("Not Found");
    }
});
server.start();
```

Adding multiple middlewares creates a chain -- call `next.run()` to invoke the next middleware in the chain. Or don't; it's up to you! Here, the last middleware in the chain only gets called if the first one doesn't find a path match:
```java
Server server = new SunHttpServer(8080);

server.use((ctx, next) -> {
    if(ctx.request().path().equals("/secret")) {
        ctx.response().body("You found the secret page!");
    }
    else {
        next.run();
    }
});

server.use((ctx, next) -> {
    ctx.response().status(404);
    ctx.response().body("Not Found");
});

server.start();
```

A middleware can act as a filter to accept or reject requests (maybe based on authentication information):
```java
Server server = new SunHttpServer(8080);

// Store Authorization header, if present as an attribute on the context
server.use((ctx, next) -> {
    String user = ctx.request().header("Authorization");
    if(user != null) {
        ctx.put("example", "user", user);
    }
    next.run();
});

//  Retrieve the stored auth information and reject any request that doesn't have the right value
server.use((ctx, next) -> {
    Optional<String> maybeUser = ctx.get("example", "user", String.class);

    if(maybeUser.isPresent()) {
        if("admin".equals(maybeUser.get())) {   // TODO: not very secure :)
            next.run();
            return;
        }
    }

    ctx.response().status(401);
});

// This middleware will only run when the previous one permits it
server.use((ctx, next) -> {
    ctx.response().body("Welcome to the administration portal!");
});

server.start();
```

Wrap calls to `next.run()` to be able to execute code before and after the rest of the chain executes:
```java
Server server = new SunHttpServer(8080);

server.use((ctx, next) -> {
   final long start = System.currentTimeMillis();
   next.run();
   final long duration = System.currentTimeMillis() - start;
   System.out.println("Request to " + ctx.request().path() + " took " + duration + "ms");
});

server.use((ctx, next) -> {
    ctx.response().body("Hello World!");
});

server.start();
```

