package ca.lavers.joa.core.util;

import ca.lavers.joa.core.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class WrappedRequest implements Request {

  protected final Request wrapped;

  protected WrappedRequest(Request wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public String path() {
    return wrapped.path();
  }

  @Override
  public String method() {
    return wrapped.method();
  }

  @Override
  public Map<String, String> queryParams() {
    return wrapped.queryParams();
  }

  @Override
  public Map<String, String> headers() {
    return wrapped.headers();
  }

  @Override
  public String header(String name) {
    return wrapped.header(name);
  }

  @Override
  public String remoteIp() {
    return wrapped.remoteIp();
  }

  @Override
  public InputStream body() throws IOException {
    return wrapped.body();
  }

  @Override
  public <T> T parseBody(Class<T> bodyType) throws IOException {
    return wrapped.parseBody(bodyType);
  }

  @Override
  public String readBody() throws IOException {
    return wrapped.readBody();
  }
}
