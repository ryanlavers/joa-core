package ca.lavers.joa.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResponse implements Response {

  protected InputStream body;
  protected long bodySize = -1;
  private ObjectMapper objectMapper = new ObjectMapper();

  protected int status = 404;
  private boolean statusSet = false;

  private Map<String, String> responseHeaders = new HashMap<>();

  @Override
  public void body(InputStream body) {
    this.body = body;
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
    return this.responseHeaders.get(name);
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
    this.responseHeaders.put(name, value);
  }

  @Override
  @JsonGetter("headers")
  public Map<String, String> headers() {
    return Collections.unmodifiableMap(this.responseHeaders);
  }
}
