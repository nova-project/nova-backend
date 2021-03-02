package net.getnova.framework.api.processor.controller;

import java.util.Map;
import net.getnova.framework.api.processor.endpoint.EndpointProcessor;
import net.getnova.framework.api.rest.annotation.RestApiController;

public class RestApiControllerProcessor extends AbstractControllerProcessor<RestApiController> {

  public RestApiControllerProcessor(final Map<String, EndpointProcessor> endpointProcessors) {
    super(endpointProcessors);
  }

  @Override
  public String getPath(final RestApiController annotation) {
    return annotation.value();
  }
}
