package ca.lavers.joa.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A chain/pipeline of {@link Middleware} that can be executed by providing
 * a {@link Context} object. This chain can itself be used as a Middleware.
 */
public class MiddlewareChain implements Middleware {

  private final List<Middleware> middlewares = new ArrayList<>();

  /**
   * Creates a new, empty chain
   */
  public MiddlewareChain() {

  }

  /**
   * Creates a new chain containing the provided middlewares, in order.
   */
  public MiddlewareChain(Middleware... middlewares) {
    this.append(middlewares);
  }

  /**
   * Creates a new chain containing the provided middlewares, in order.
   */
  public MiddlewareChain(List<Middleware> middlewares) {
    middlewares.forEach(this::append);
  }

  /**
   * Appends one or more additional middlewares to the end of this chain
   */
  public void append(Middleware... middlewares) {
    for (Middleware m : middlewares) {
      if (m.getClass().equals(MiddlewareChain.class)) {
        MiddlewareChain mc = (MiddlewareChain) m;
        mc.middlewares.forEach(this::append);
      } else {
        this.middlewares.add(m);
      }
    }
  }

  /**
   * Executes this middleware chain, passing the provided Context object
   * to the first Middleware in the chain.
   */
  public void call(Context ctx) {
    getMiddleware(0, ctx, null).run();
  }

  @Override
  public void call(Context ctx, NextMiddleware next) {
    getMiddleware(0, ctx, next).run();
  }

  private NextMiddleware getMiddleware(int index, Context ctx, NextMiddleware next) {
    if(index >= 0 && index < middlewares.size()) {
      return new NextMiddleware() {
        public void runWithAlternateContext(Context ctx) {
          middlewares.get(index).call(ctx, getMiddleware(index+1, ctx, next));
        }

        public void run() {
          middlewares.get(index).call(ctx, getMiddleware(index+1, ctx, next));
        }
      };
    }
    else {
      if(next != null) {
        return next;
      }
      else {
        // Empty middleware
        return new NextMiddleware() {
          public void runWithAlternateContext(Context ctx) { }
          public void run() { }
        };
      }
    }
  }

}
