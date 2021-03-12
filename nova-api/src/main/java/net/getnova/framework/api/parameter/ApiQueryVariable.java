package net.getnova.framework.api.parameter;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.ApiException;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.request.ApiRequest;
import net.getnova.framework.api.data.response.ApiError;
import net.getnova.framework.api.parser.ApiParameterParser;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiQueryVariable {

  String value();

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  class Parameter<T> implements ApiParameter<T> {

    private final ConversionService conversionService;
    private final Class<T> clazz;
    private final String name;

    @Override
    public T parse(final ApiRequest request) {
      try {
        // TODO: better solution
        if (!Collection.class.isAssignableFrom(this.clazz)) {
          return this.conversionService.convert(
            Optional.ofNullable(request.getQueryVariable(this.name))
              .flatMap(list -> list.size() == 0
                ? Optional.empty()
                : Optional.ofNullable(list.get(0))
              )
              .orElse(null),
            this.clazz);
        }
        return this.conversionService.convert(request.getQueryVariable(this.name), this.clazz);
      }
      catch (ConversionException e) {
        throw ApiException.of(HttpResponseStatus.BAD_REQUEST, e, ApiError.of(this.name, "UNABLE_TO_CONVERT"));
      }
    }
  }

  @Data
  class Parser implements ApiParameterParser<ApiQueryVariable, Parameter<?>> {

    private static final Class<ApiQueryVariable> ANNOTATION = ApiQueryVariable.class;
    private final ConversionService conversionService;

    @Override
    public Class<ApiQueryVariable> getAnnotationType() {
      return ANNOTATION;
    }

    @Override
    public Parameter<?> parse(final ApiQueryVariable annotation, final java.lang.reflect.Parameter parameter) {
      if (!this.conversionService.canConvert(String.class, parameter.getType())) {
        throw new IllegalArgumentException(String.format("Can not find converter for \"%s -> %s\".",
          String.class.getName(), parameter.getType().getName()));
      }

      return new Parameter<>(this.conversionService, parameter.getType(), annotation.value());
    }
  }
}
