package ca.lavers.joa.core;

import ca.lavers.joa.core.errors.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Base class providing some methods likely common to all {@link Request} implementations.
 */
public abstract class AbstractRequest implements Request {

  private ObjectMapper objectMapper = new ObjectMapper();
  private Object parsedBody;

  /**
   * Splits the given query string into a Map of key/value pairs.
   * If multiple identical keys exist in the string, only the value associated with
   * the last occurence will be taken.
   */
  protected static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
  }

  @Override
  public Map<String, String> queryParams() {
    String query = rawQuery();
    if(query != null) {
      try {
        return splitQuery(query);
      } catch (Exception e) {
        // TODO - Pass e to BadRequestException when it takes it
        throw new BadRequestException("Unable to parse query string");
      }
    }
    return new HashMap<>();
  }

  /**
   * @inheritDoc
   *
   * Default implementation that compares the given name case-insensitively
   * against every header name provided by {@link #headers()}. Extending classes
   * should override this if they can do it more efficiently.
   */
  @Override
  public String header(String name) {
    for(Map.Entry<String, String> entry : this.headers().entrySet()) {
      if(entry.getKey().toLowerCase(Locale.ROOT).equals(name)) {
        return entry.getValue();
      }
    }
    return null;
  }

  // TODO - cache body if it's been read as a string or object
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
    if(parsedBody != null && parsedBody.getClass().isAssignableFrom(bodyType)) {
      return (T) parsedBody;
    }
    try {
      parsedBody = objectMapper.readValue(body(), bodyType);
      return (T) parsedBody;
    } catch (JsonProcessingException e) {
      throw new IOException("Unable to parse request body", e);
    }
  }
}
