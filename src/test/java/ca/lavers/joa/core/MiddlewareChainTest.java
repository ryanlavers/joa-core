package ca.lavers.joa.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MiddlewareChainTest {

  // Simple test middleware that records whether or not it has been run
  private class TestMiddleware implements Middleware {
    boolean ran = false;
    public void call(Context ctx, NextMiddleware next) {
      ran = true;
      next.run();
    }
  }

  @Test
  void runsMiddleware() {
    TestMiddleware a = new TestMiddleware(), b = new TestMiddleware();
    MiddlewareChain chain = new MiddlewareChain(a, b);

    chain.call(null);

    assertTrue(a.ran);
    assertTrue(b.ran);
  }

  @Test
  void runsMiddlewareInOrder() {
    List<String> results = new ArrayList<>();

    MiddlewareChain chain = new MiddlewareChain(
        (ctx, next) -> { results.add("A"); next.run(); },
        (ctx, next) -> { results.add("B"); next.run(); }
    );

    chain.call(null);

    assertEquals(2, results.size());
    assertEquals("A", results.get(0));
    assertEquals("B", results.get(1));
  }

  @Test
  void  middlewareCanStopChain() {
    TestMiddleware second = new TestMiddleware();

    MiddlewareChain chain = new MiddlewareChain(
        (ctx, next) -> { /* No next.run() call! */ },
        second
    );

    chain.call(null);

    assertFalse(second.ran);
  }

  @Test
  void contextIsShared() {
    Context context = new Context(null, null);

    MiddlewareChain chain = new MiddlewareChain(
        (ctx, next) -> { assertSame(context, ctx); next.run(); },
        (ctx, next) -> { assertSame(context, ctx); next.run(); }
    );

    chain.call(context);
  }

  @Test
  void middlewareChainIsMiddleware() {
    TestMiddleware
        a = new TestMiddleware(),
        b = new TestMiddleware(),
        c = new TestMiddleware();

    MiddlewareChain innerChain = new MiddlewareChain(a, b);
    MiddlewareChain outerChain = new MiddlewareChain(innerChain, c);

    outerChain.call(null);

    assertTrue(a.ran);
    assertTrue(b.ran);
    assertTrue(c.ran);
  }

  @Test
  void innerChainCanStopOuterChain() {
    TestMiddleware
        a = new TestMiddleware(),
        b = new TestMiddleware();

    MiddlewareChain innerChain = new MiddlewareChain(
        a,
        (ctx, next) -> { /* No next.run() call */ }
    );

    MiddlewareChain outerChain = new MiddlewareChain(innerChain, b);

    outerChain.call(null);

    assertTrue(a.ran);
    assertFalse(b.ran);
  }

  @Test
  void canRunWithAlternateContext() {
    Context initialContext = new Context(null, null);
    Context newContext = new Context(null, null);

    MiddlewareChain chain = new MiddlewareChain(
        (ctx, next) -> {
          assertSame(initialContext, ctx);
          next.runWithAlternateContext(newContext);
        },
        (ctx, next) -> {
          assertSame(newContext, ctx);
          next.run();
        },
        (ctx, next) -> {
          assertSame(newContext, ctx);
        }
    );

    chain.call(initialContext);
  }

  @Test
  void emptyChainStillCallsNext() {
    TestMiddleware last = new TestMiddleware();

    MiddlewareChain emptyChain = new MiddlewareChain();
    MiddlewareChain outer = new MiddlewareChain(emptyChain, last);

    outer.call(null);
    assertTrue(last.ran);
  }

}
