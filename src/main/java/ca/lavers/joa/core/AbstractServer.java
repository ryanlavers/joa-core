package ca.lavers.joa.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class implementing behaviour that is likely common to
 * all {@link Server} implementations.
 *
 * In particular, it handles everything related to {@link Middleware};
 * implementing classes just need to call {@link #callMiddleware(Context)}
 * with a Context representing the request, and then writing out the
 * response afterward.
 */
public abstract class AbstractServer implements Server {

  private static final Logger log = LoggerFactory.getLogger(AbstractServer.class);

  private final MiddlewareChain middleware = new MiddlewareChain();

  @Override
  public void use(Middleware middleware) {
    this.middleware.append(middleware);
  }

  /**
   * Calls the configured middleware chain with the given Context, which
   * must contain a {@link Request} object representing the current request,
   * and a {@link Response} object that can be filled by the middleware
   * with response content.
   *
   * Afterwards, caller can assume the context's response has been
   * filled and can be written back out to the client. If the middleware
   * chain throws any exceptions, the response is set to a 500 Internal
   * Server Error.
   */
  protected void callMiddleware(Context ctx) {
    try {
      this.middleware.call(ctx);
    } catch (Exception e) {
      log.atError().setCause(e).log("Calling middleware chain threw exeption");
      ctx.response().status(500);
      ctx.response().body("Internal Server Error");
    }
  }
}
