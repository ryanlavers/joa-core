package ca.lavers.joa.core;

import java.util.Optional;

/**
 * Passed to {@link Middleware} when invoked to provide access to the {@link Request} and
 * {@link Response} objects for the current request, as well as being a store for request-
 * specific attributes to allow for communication between middleware. Attributes are
 * organized under namespaces to help avoid unintentional clobbering of values by middleware
 * accidentally using the same attribute names. Each middleware (or set of related middleware)
 * should choose a namespace that is unlikely to be used by anyone else, such as its full
 * package and class name (e.g. com.example.middleware.MyCoolMiddleware).
 */
public class Context {

  private final Request request;
  private final Response response;
  private final NamespacedAttributeStore attributes;

  /**
   * Creates a new Context with the provided request and response and an
   * empty attribute store
   */
  public Context(Request request, Response response) {
    this.request = request;
    this.response = response;
    this.attributes = new NamespacedAttributeStore();
  }

  /**
   * Creates a new Context with the provided request, response, and
   * attribute store
   */
  public Context(Request request, Response response, NamespacedAttributeStore attributes) {
    this.request = request;
    this.response = response;
    this.attributes = attributes;
  }

  /**
   * Returns the Request object for the current request
   */
  public Request request() {
    return this.request;
  }

  /**
   * Returns the Response object for the current request
   */
  public Response response() {
    return this.response;
  }

  /**
   * Returns the attribute store contained in this context
   */
  public NamespacedAttributeStore attributes() {
    return this.attributes;
  }

  /**
   * Retrieves the value, if any, associated with the specified context attribute. If
   * no such attribute value is found, or it is not of the expected type, an empty
   * Optional is returned.
   *
   * @param namespace The attribute namespace to look under
   * @param key The attribute name
   * @param expectedType The expected type of the value
   * @return An Optional either containing the value, or empty if not found or not of the expected type
   */
  public <T> Optional<T> get(String namespace, String key, Class<T> expectedType) {
    return attributes.get(namespace, key, expectedType);
  }

  /**
   * Sets the value of the specified context attribute
   *
   * @param namespace The attribute namespace to use
   * @param key The attribute name to set the value of
   * @param value The value to store
   */
  public void put(String namespace, String key, Object value) {
    attributes.put(namespace, key, value);
  }

  /**
   * Removes any value associated with the specified context attribute,
   *
   * @param namespace The attribute namespace to look under
   * @param key The attribute name whose value should be removed
   */
  public void remove(String namespace, String key) {
    attributes.remove(namespace, key);
  }

  /**
   * Returns a new Context with the same Response and attribute store
   * as this one, but with an alternate Request object.
   */
  public Context withAlternateRequest(Request request) {
    return new Context(request, this.response, this.attributes);
  }

  /**
   * Returns a new Context with the same Request and attribute store
   * as this one, but with an alternate Response object.
   */
  public Context withAlternateResponse(Response response) {
    return new Context(this.request, response, this.attributes);
  }
}
