package ca.lavers.joa.core;

import java.io.IOException;

public interface Server {

  void use(Middleware middleware);
  void start() throws IOException;
  void stop() throws IOException;

}
