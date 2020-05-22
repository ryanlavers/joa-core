package ca.lavers.joa.sunhttp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import ca.lavers.joa.core.AbstractRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SunHttpRequest extends AbstractRequest {

  private final HttpExchange xchg;

  private Map<String, String> cachedHeaders;

  SunHttpRequest(HttpExchange xchg) {
    this.xchg = xchg;
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
  public Map<String, String> queryParams() {
    String query = xchg.getRequestURI().getQuery();
    if(query != null) {
      try {
        return splitQuery(query);
      } catch (UnsupportedEncodingException e) {
      }
    }
    return new HashMap<>();
  }

  private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
  }

  @Override
  public Map<String, String> headers() {
    if(this.cachedHeaders == null) {
      Headers headers = xchg.getRequestHeaders();
      Map<String, String> map = new HashMap<>();
      headers.forEach((key, values) -> {
        map.put(key, values.get(values.size() - 1));    // TODO -- only taking the last defined value
      });
      this.cachedHeaders = map;
    }

    return this.cachedHeaders;
  }

  @Override
  public String header(String name) {
    return headers().get(name);
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
