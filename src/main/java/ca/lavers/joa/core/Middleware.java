package ca.lavers.joa.core;

/**
 * The basic building block of a request processing pipeline. Middlewares can be appended
 * to a Server's main Middleware chain with the {@link Server#use(Middleware)} method.
 * The first Middleware in this chain will be called for each request processed; that
 * Middleware can optionally invoke the next Middleware in the chain.
 */
@FunctionalInterface
public interface Middleware {
  /**
   * Called when this Middleware is invoked.
   *
   * The request context object provides access to the {@link Request} and {@link Response}
   * objects, as well as request-specific attributes for passing information between
   * Middleware.
   *
   * To invoke the next Middleware in the chain, call next.run(). It will automatically
   * be given the same Context instance as this Middleware was, unless a different one
   * is passed with next.runWithAlternateContext(context) instead.
   *
   * @param ctx The request context
   * @param next A function to invoke the next Middleware in the chain, if desired
   */
  void call(Context ctx, NextMiddleware next);
}