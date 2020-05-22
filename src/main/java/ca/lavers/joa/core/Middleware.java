package ca.lavers.joa.core;

@FunctionalInterface
public interface Middleware {
  void call(Context ctx, NextMiddleware next);
}