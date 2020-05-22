package ca.lavers.joa.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Request {

  String path();
  String method();

  Map<String, String> queryParams();

  Map<String, String> headers();
  String header(String name);

  String remoteIp();

  InputStream body() throws IOException;
  
  <T> T parseBody(Class<T> bodyType) throws IOException;
  String readBody() throws IOException;

}
