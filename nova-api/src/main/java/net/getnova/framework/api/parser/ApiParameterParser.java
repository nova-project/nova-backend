package net.getnova.framework.api.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Optional;
import net.getnova.framework.api.data.ApiParameter;

public interface ApiParameterParser<A extends Annotation, P extends ApiParameter<?>> {

  Class<A> getAnnotationType();

  default Optional<P> parse(Parameter parameter) {
    return Optional.ofNullable(parameter.getAnnotation(this.getAnnotationType()))
      .map(annotation -> this.parse(annotation, parameter));
  }

  P parse(A annotation, Parameter parameter);
}
