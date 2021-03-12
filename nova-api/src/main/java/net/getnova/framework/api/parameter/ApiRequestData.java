package net.getnova.framework.api.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.request.ApiRequest;
import net.getnova.framework.api.parser.ApiParameterParser;
import reactor.core.publisher.Mono;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequestData {

  @Data
  @Slf4j
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  class Parameter<T> implements ApiParameter<Mono<T>> {

    private final Class<T> clazz;

    @Override
    public Mono<T> parse(final ApiRequest request) {
      return request.getData(this.clazz);
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  class Parser implements ApiParameterParser<ApiRequestData, Parameter<?>> {

    public static final Parser INSTANCE = new Parser();
    private static final Class<ApiRequestData> ANNOTATION = ApiRequestData.class;

    @Override
    public Class<ApiRequestData> getAnnotationType() {
      return ANNOTATION;
    }

    @Override
    public Parameter<?> parse(final ApiRequestData annotation, final java.lang.reflect.Parameter parameter) {
      return new Parameter<>(parameter.getType());
    }
  }
}
