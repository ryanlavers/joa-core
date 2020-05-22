package ca.lavers.joa.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public abstract class AbstractRequest implements Request {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String readBody() throws IOException {
    String text;
    try (Scanner scanner = new Scanner(body(), StandardCharsets.UTF_8.name())) {
      text = scanner.useDelimiter("\\A").next();
      scanner.close();
      return text;
    }
  }

  @Override
  public <T> T parseBody(Class<T> bodyType) throws IOException {
    try {
      return objectMapper.readValue(body(), bodyType);
    } catch (JsonProcessingException e) {
      throw new IOException("Unable to parse request body", e);
    }
  }
}
