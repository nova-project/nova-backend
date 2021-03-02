package net.getnova.framework.api.data;

import io.netty.handler.codec.http.HttpMethod;
import java.util.Map;
import net.getnova.framework.api.exception.ParserApiException;

public interface ApiRequest {

  HttpMethod getMethod();

  String getPath();

  Map<String, String> getPathVariables();

  void setPathVariables(Map<String, String> variables);

  String getPathVariable(String name);

  <T> T getData(Class<T> clazz) throws ParserApiException;
}
