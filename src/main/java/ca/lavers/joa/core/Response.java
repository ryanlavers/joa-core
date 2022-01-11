package ca.lavers.joa.core;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An HTTP response to be returned to the client
 */
public interface Response {

  /**
   * Returns the HTTP status code that will be sent with this response
   */
  @JsonGetter("status")
  int status();

  /**
   * Sets the HTTP status code that will be sent with this response. If
   * not explicitly set, defaults to 204 if no body has been set, or 200
   * if one has.
   */
  void status(int status);

  /**
   * Sets the body that will be sent with this response. Automatically sets
   * the appropriate Content-Length response header. Status code will be
   * set to 200 if it hasn't already been set.
   */
  void body(String body);

  /**
   * Set an InputStream that will be read completely to provide the
   * body that will be sent with this response. Content-Length header will NOT
   * be set by this method (likely resulting in a chunked/streamed response).
   * Call {@link #bodySize(long)} in addition to this method if you know (and
   * want to provide) the size of the body that this InputStream will produce.
   * Status code will be set to 200 if it hasn't already been set.
   */
  // TODO - Add size parameter
  void body(InputStream body);

  /**
   * Set an object that will be serialized to JSON and used as the body that
   * will be sent with this response. Automatically sets the appropriate
   * Content-Type and Content-Length response headers. Status code will be
   * set to 200 if it hasn't already been set.
   */
  void body(Object body) throws IOException;

  /**
   * Returns the size of the response body, if known or has been set. A value
   * of -1 indicates the body size is unknown, and will be sent as a "chunked"
   * response.
   */
  long bodySize();

  /**
   * Manually sets the response body size that will be used as the Content-Length
   * response header. It is recommended to only do this after setting an InputStream
   * as the response body if you know and wish to provide its actual length;
   * otherwise the response will be sent as a "chunked" response. If a String or
   * Object is provided as the body instead, the size is automatically calculated.
   * If this method is called with -1 after providing a String or Object body, it
   * will override the calculated size and send a chunked response anyway.
   *
   * @param size The value to be sent in the Content-Length response header; or -1 to
   *             stream the response body as a chunked response.
   */
  void bodySize(long size);

  /**
   * Returns a map of response headers and values set so far. The returned header
   * names will probably be exactly as they were set, but as HTTP header names
   * are case-insensitive, implementations may change case on you. The returned
   * map should be considered (and probably is) immutable.
   */
  @JsonGetter("headers")
  Map<String, String> headers();

  /**
   * Returns the value of a previously set response header, if present.
   *
   * @param name The name of the response header to retrieve; case insensitive
   * @return The value of the header, or null if not present
   */
  String header(String name);

  /**
   * Sets a header to be sent with the response. If value is null, any existing
   * value will be removed and this header will not be sent (unless subsequently
   * set to a new value).
   *
   * @param name The name of the response header to set
   * @param value The value of the header, or null to remove this header
   */
  void header(String name, String value);
}

