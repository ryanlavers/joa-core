package ca.lavers.joa.core;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Response {

  @JsonGetter("status")
  int status();
  void status(int status);

  void body(String body);
  void body(InputStream body);
  void body(Object body) throws IOException;

  long bodySize();
  void bodySize(long size);

  @JsonGetter("headers")
  Map<String, String> headers();
  String header(String name);
  void header(String name, String value);
}

