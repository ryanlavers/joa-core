package ca.lavers.joa.core;

public interface NextMiddleware extends Runnable {
  void runWithAlternateContext(Context ctx);
}
