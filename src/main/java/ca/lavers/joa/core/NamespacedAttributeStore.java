package ca.lavers.joa.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class NamespacedAttributeStore {

  private final Map<String, Map<String, Object>> attributes = new HashMap<>();

  public Set<String> namespaces() {
    return attributes.keySet();
  }

  public Set<String> attributes(String namespace) {
    Map<String, Object> nsAttrs = attributes.get(namespace);
    if(nsAttrs != null) {
      return nsAttrs.keySet();
    }
    else {
      return null;
    }
  }

  public <T> Optional<T> get(String namespace, String key, Class<T> expectedType) {
    Map<String, Object> nsAttrs = attributes.get(namespace);
    if(nsAttrs != null) {
      Object value = nsAttrs.get(key);
      if (value != null && expectedType.isAssignableFrom(value.getClass())) {
        return Optional.of(expectedType.cast(value));
      }
    }
    return Optional.empty();
  }

  public void put(String namespace, String key, Object value) {
    Map<String, Object> nsAttrs =
        attributes.computeIfAbsent(namespace, (k) -> new HashMap<>());

    nsAttrs.put(key, value);
  }

  public void remove(String namespace, String key) {
    Map<String, Object> nsAttrs = attributes.get(namespace);
    if(nsAttrs != null) {
      nsAttrs.remove(key);
    }
  }
}
