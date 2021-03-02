package net.getnova.framework.api.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.exception.ParameterApiException;
import net.getnova.framework.api.exception.RuntimeApiException;
import net.getnova.framework.api.parser.ApiParameterParser;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPathVariable {

  String value();

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  class Parameter<T> implements ApiParameter<T> {

    private final ConversionService conversionService;
    private final Class<T> clazz;
    private final String name;

    @Override
    public T parse(final ApiRequest request) throws ParameterApiException {
      try {
        return this.conversionService.convert(request.getPathVariable(this.name), this.clazz);
      }
      catch (ConversionException e) {
        throw new ParameterApiException("UNABLE_TO_CONVERT", this.name, e);
      }
    }
  }

  @Data
  class Parser implements ApiParameterParser<ApiPathVariable, Parameter<?>> {

    private static final Class<ApiPathVariable> ANNOTATION = ApiPathVariable.class;
    private final ConversionService conversionService;

    @Override
    public Class<ApiPathVariable> getAnnotationType() {
      return ANNOTATION;
    }

    @Override
    public Parameter<?> parse(final ApiPathVariable annotation, final java.lang.reflect.Parameter parameter) {
      if (!this.conversionService.canConvert(String.class, parameter.getType())) {
        throw new RuntimeApiException(String.format("Can not find converter for \"%s -> %s\".",
          String.class.getName(), parameter.getType().getName()));
      }

      return new Parameter<>(this.conversionService, parameter.getType(), annotation.value());
    }
  }
}
