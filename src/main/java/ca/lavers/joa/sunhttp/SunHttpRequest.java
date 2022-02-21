package ca.lavers.joa.sunhttp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import ca.lavers.joa.core.AbstractRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SunHttpRequest extends AbstractRequest {

  private final HttpExchange xchg;
  private final Headers headers;

  private Map<String, String> cachedHeaders;

  SunHttpRequest(HttpExchange xchg) {
    this.xchg = xchg;
    this.headers = xchg.getRequestHeaders();
  }

  @Override
  public String path() {
    return xchg.getRequestURI().getPath();
  }

  @Override
  public String method() {
    return xchg.getRequestMethod().toUpperCase();
  }

  @Override
  public String rawQuery() {
    return xchg.getRequestURI().getQuery();
  }

  // TODO -- combine all values of a multi-valued header with comma
  // https://stackoverflow.com/questions/3096888/standard-for-adding-multiple-values-of-a-single-http-header-to-a-request-or-resp

  @Override
  public Map<String, String> headers() {
    if(this.cachedHeaders == null) {
      Map<String, String> map = new HashMap<>();
      headers.forEach((key, values) -> {
        map.put(key, values.get(values.size() - 1));
      });
      this.cachedHeaders = map;
    }

    return this.cachedHeaders;
  }

  @Override
  public String header(String name) {
    return headers.getFirst(name);
  }

  @Override
  public String remoteIp() {
    return xchg.getRemoteAddress().getAddress().getHostAddress();
  }

  @Override
  public InputStream body() throws IOException {
    return xchg.getRequestBody();
  }

}
