package net.getnova.framework.api.processor.endpoint;

import javax.lang.model.element.ExecutableElement;

public interface EndpointProcessor {

  void process(ExecutableElement method);
}
