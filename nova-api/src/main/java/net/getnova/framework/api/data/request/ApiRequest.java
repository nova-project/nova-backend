package net.getnova.framework.api.data.request;

import io.netty.handler.codec.http.HttpMethod;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public interface ApiRequest {

  HttpMethod getMethod();

  String getPath();

  Map<String, String> getPathVariables();

  void setPathVariables(Map<String, String> variables);

  MultiValueMap<String, String> getQueryVariables();

  String getPathVariable(String name);

  List<String> getQueryVariable(String name);

  <T> Mono<T> getData(Class<T> clazz);
}
