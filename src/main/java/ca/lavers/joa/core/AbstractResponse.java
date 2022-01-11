package ca.lavers.joa.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Default implementation of {@link Response}
 */
public abstract class AbstractResponse implements Response {

  protected InputStream body;
  protected long bodySize = 0;

  private static final ObjectMapper objectMapper;
  static {
    objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  protected int status = 204;
  private boolean statusSet = false;

  private final Map<String, String> responseHeaders = new HashMap<>();

  @Override
  public void body(InputStream body) {
    this.body = body;
    // TODO -- don't reset bodySize if it's already been set
    this.bodySize = -1;
    if(!statusSet) {
      this.status = 200;
    }
  }

  @Override
  public void body(String body) {
    if(body != null) {
      this.body = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
      this.bodySize = body.length();
      if(!statusSet) {
        this.status = 200;
      }
    }
  }

  @Override
  public void body(Object body) throws IOException {
    // TODO - reset this header if a different body method is subsequently called
    header("Content-Type", "application/json");
    String bodyStr = objectMapper.writeValueAsString(body);
    this.body(new ByteArrayInputStream(bodyStr.getBytes(StandardCharsets.UTF_8)));
    this.bodySize = bodyStr.length();
    if(!statusSet) {
      this.status = 200;
    }
  }

  @Override
  public long bodySize() {
    return bodySize;
  }

  @Override
  public void bodySize(long size) {
    this.bodySize = size;
  }

  @Override
  public String header(String name) {
    String nameLc = name.toLowerCase(Locale.ROOT);
    for(Map.Entry<String, String> entry : this.responseHeaders.entrySet()) {
      if(entry.getKey().toLowerCase(Locale.ROOT).equals(nameLc)) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public int status() {
    return status;
  }

  @Override
  public void status(int status) {
    this.status = status;
    this.statusSet = true;
  }

  @Override
  public void header(String name, String value) {
    if(value == null) {
      this.responseHeaders.remove(name);
    }
    else {
      this.responseHeaders.put(name, value);
    }
  }

  @Override
  @JsonGetter("headers")
  public Map<String, String> headers() {
    return Collections.unmodifiableMap(this.responseHeaders);
  }
}
