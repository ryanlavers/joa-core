package ca.lavers.joa.core;

public abstract class AbstractServer implements Server {

  private final MiddlewareChain middleware = new MiddlewareChain();

  @Override
  public void use(Middleware middleware) {
    this.middleware.append(middleware);
  }

  protected void callMiddleware(Context ctx) {
      this.middleware.call(ctx);
  }
}
