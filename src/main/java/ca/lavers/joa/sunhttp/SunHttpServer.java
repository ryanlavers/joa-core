package ca.lavers.joa.sunhttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ca.lavers.joa.core.AbstractServer;
import ca.lavers.joa.core.Context;
import ca.lavers.joa.core.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A implementation of {@link Server} that uses the
 * JDK's built-in {@link HttpServer}.
 *
 */
public class SunHttpServer extends AbstractServer {

  private static final Logger log = LoggerFactory.getLogger(SunHttpServer.class);

  private final int port;
  private HttpServer server;

  private final ExecutorService executor;

  /**
   * Create a new, single-threaded server that listens on the given port.
   */
  public SunHttpServer(int port) {
    this(port, 0);
  }

  /**
   * Create a new server that listens on the given port.
   *
   * If numThreads is >1, server will be multi-threaded using a thread pool
   * of that size.
   */
  public SunHttpServer(int port, int numThreads) {
    this.port = port;
    if(numThreads < 2) {
      log.atDebug().log("Creating single-threaded server");
      this.executor = null;
    }
    else {
      log.atDebug().log("Creating multi-threaded server with {} threads", numThreads);
      this.executor = Executors.newFixedThreadPool(numThreads);
    }
  }

  public void start() throws IOException {
    log.atInfo().log("Starting server");
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new RequestHandler());
    server.setExecutor(this.executor);
    server.start();
    log.atInfo().addArgument(port).log("Listening for requests on port {}");
  }

  public void stop() throws IOException {
    log.atDebug().log("Stopping server...");
    this.server.stop(0);
    log.atInfo().log("Server stopped");
  }

  private class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      SunHttpResponse resp = new SunHttpResponse();
      Context ctx = new Context(new SunHttpRequest(t), resp);
      try {
        callMiddleware(ctx);

        resp.headers().forEach((key, value) -> {
          if(value != null) {
            t.getResponseHeaders().add(key, value);
          }
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
