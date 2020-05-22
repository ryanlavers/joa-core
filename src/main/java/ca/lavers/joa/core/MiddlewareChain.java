package ca.lavers.joa.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiddlewareChain implements Middleware {

  private List<Middleware> middlewares = new ArrayList<>();

  public MiddlewareChain() {

  }

  public MiddlewareChain(Middleware... middlewares) {
    this(Arrays.asList(middlewares));
  }

  public MiddlewareChain(List<Middleware> middlewares) {
    this.middlewares.addAll(middlewares);
  }

  public void append(Middleware middleware) {
    this.middlewares.add(middleware);
  }

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
