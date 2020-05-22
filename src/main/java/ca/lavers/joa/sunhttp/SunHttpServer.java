package ca.lavers.joa.sunhttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ca.lavers.joa.core.AbstractServer;
import ca.lavers.joa.core.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SunHttpServer extends AbstractServer {

  private final int port;
  private HttpServer server;

  public SunHttpServer(int port) {
    this.port = port;
  }

  @Override
  public void start() throws IOException {
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new RequestHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
  }

  @Override
  public void stop() throws IOException {
    this.server.stop(0);
  }

  private class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      SunHttpResponse resp = new SunHttpResponse();
      Context ctx = new Context(new SunHttpRequest(t), resp);
      try {
        callMiddleware(ctx);

        resp.headers().forEach((key, value) -> {
          t.getResponseHeaders().add(key, value);
        });

        long respLength = resp.bodySize();

        // The Sun HttpServer wants -1 to mean "no response" (i.e. zero-length) and 0 to mean "unknown length" (chunked response)...
        // So we'll swap those around here to restore sanity to the universe :)
        if (respLength == -1) {
          respLength = 0;
        } else if (respLength == 0) {
          respLength = -1;
        }

        t.sendResponseHeaders(resp.status(), respLength);

        OutputStream out = t.getResponseBody();
        resp.getResponseStream().transferTo(out);
      }
      finally {
        resp.getResponseStream().close();
        t.getResponseBody().close();
      }
    }
  }
}
