package ca.lavers.joa.core;

import java.util.Optional;

public class Context {

  private final Request request;
  private final Response response;
  private final NamespacedAttributeStore attributes = new NamespacedAttributeStore();

  public Context(Request request, Response response) {
    this.request = request;
    this.response = response;
  }

  public Request request() {
    return this.request;
  }

  public Response response() {
    return this.response;
  }

  public NamespacedAttributeStore attributes() {
    return this.attributes;
  }

  public <T> Optional<T> get(String namespace, String key, Class<T> expectedType) {
    return attributes.get(namespace, key, expectedType);
  }

  public void put(String namespace, String key, Object value) {
    attributes.put(namespace, key, value);
  }

  public void remove(String namespace, String key) {
    attributes.remove(namespace, key);
  }
}
