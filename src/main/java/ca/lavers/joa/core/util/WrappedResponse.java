package ca.lavers.joa.core.util;

import ca.lavers.joa.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class WrappedResponse implements Response {

  protected final Response wrapped;

  protected WrappedResponse(Response wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public int status() {
    return wrapped.status();
  }

  @Override
  public void status(int status) {
    wrapped.status(status);
  }

  @Override
  public void body(Object body) throws IOException {
    wrapped.body(body);
  }

  @Override
  public void header(String name, String value) {
    wrapped.header(name, value);
  }

  @Override
  public Map<String, String> headers() {
    return wrapped.headers();
  }

  @Override
  public void body(String body) {
    wrapped.body(body);
  }

  @Override
  public void body(InputStream body) {
    wrapped.body(body);
  }

  @Override
  public long bodySize() {
    return wrapped.bodySize();
  }

  @Override
  public void bodySize(long size) {
    wrapped.bodySize(size);
  }

  @Override
  public String header(String name) {
    return wrapped.header(name);
  }
}
