package net.getnova.framework.api.ws.parser;

import java.util.Optional;
import net.getnova.framework.api.data.ApiControllerMetadata;
import net.getnova.framework.api.parser.AbstractApiControllerParser;
import net.getnova.framework.api.ws.annotation.WebsocketApiController;

public class WebsocketApiControllerParser extends AbstractApiControllerParser {

  @Override
  public Optional<ApiControllerMetadata> parse(final Class<?> clazz) {
    return Optional.ofNullable(clazz.getAnnotation(WebsocketApiController.class))
      .map(controller -> new ApiControllerMetadata(controller.value()));
  }
}
