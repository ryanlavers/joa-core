package ca.lavers.joa.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An incoming HTTP request
 */
public interface Request {

  /**
   * Returns the request path (not including the query string)
   */
  String path();

  /**
   * Returns the HTTP method (GET, POST, PUT, etc.) as an uppercase string
   */
  String method();

  /**
   * Returns the request query string as a map of key/value pairs
   */
  Map<String, String> queryParams();

  /**
   * Returns all request headers as a map of name/value pairs. HTTP header names
   * are case-insensitive, so this method may return the names in a normalized
   * format (e.g. all-lowercase, all-uppercase, or something in between) rather
   * than exactly as provided in the request, depending on the {@link Server}
   * implementation in use.
   */
  Map<String, String> headers();

  /**
   * Returns the value of the named header (case-insensitively) or null
   * if there is no such header present on the request.
   */
  String header(String name);

  /**
   * Returns the IP address of the client making the request
   */
  String remoteIp();

  /**
   * Returns an InputStream from which the body of the request can be read.
   * @throws IOException If the request body cannot be read
   */
  InputStream body() throws IOException;

  /**
   * Reads and parses the request body into an instance of the given class
   *
   * @param bodyType The class that the body should be deserialized as
   * @return The deserialized body as an instance of the bodyType class
   * @throws IOException If the request body cannot be read, or if it cannot be
   *                     deserialized as an instance of the provided class
   */
  <T> T parseBody(Class<T> bodyType) throws IOException;

  /**
   * Reads and returns the request body as a String
   *
   * @return The request body as a String
   * @throws IOException If the request body cannot be read
   */
  String readBody() throws IOException;

}
