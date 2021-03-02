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
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.exception.ParameterApiException;
import net.getnova.framework.api.exception.ParserApiException;
import net.getnova.framework.api.parser.ApiParameterParser;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequestData {

  @Data
  @Slf4j
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  class Parameter<T> implements ApiParameter<T> {

    private final Class<T> clazz;

    @Override
    public T parse(final ApiRequest request) throws ParameterApiException {
      try {
        return request.getData(this.clazz);
      }
      catch (ParserApiException e) {
        log.error("Unable to parse parameter.", e);
        throw new ParameterApiException("INVALID_REQUEST_DATA", e);
      }
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
