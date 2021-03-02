package net.getnova.framework.api.rest.parser;

import java.util.Optional;
import net.getnova.framework.api.data.ApiControllerMetadata;
import net.getnova.framework.api.parser.AbstractApiControllerParser;
import net.getnova.framework.api.rest.annotation.RestApiController;

public class RestApiControllerParser extends AbstractApiControllerParser {

  @Override
  public Optional<ApiControllerMetadata> parse(final Class<?> clazz) {
    return Optional.ofNullable(clazz.getAnnotation(RestApiController.class))
      .map(controller -> new ApiControllerMetadata(controller.value()));
  }
}
