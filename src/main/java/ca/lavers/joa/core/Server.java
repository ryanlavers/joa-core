package ca.lavers.joa.core;

import java.io.IOException;

/**
 * An HTTP server that handles requests using a chain of {@link Middleware}
 */
public interface Server {

  /**
   * Adds the given middleware to this Server's chain.
   *
   * When an HTTP request is received, it will be passed to the first added
   * middleware. If that middleware so chooses (by calling {@code next.run()}),
   * the next middleware in the chain will be called, and so on.
   */
  void use(Middleware middleware);

}
