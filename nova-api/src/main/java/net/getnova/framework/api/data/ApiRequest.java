package net.getnova.framework.api.data;

import io.netty.handler.codec.http.HttpMethod;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface ApiRequest {

  HttpMethod getMethod();

  String getPath();

  Map<String, String> getPathVariables();

  void setPathVariables(Map<String, String> variables);

  String getPathVariable(String name);

  <T> Mono<T> getData(Class<T> clazz);
}
