package net.getnova.framework.api.processor.endpoint;

import javax.lang.model.element.ExecutableElement;

public abstract class AbstractEndpointProcessor implements EndpointProcessor {

  @Override
  public void process(final ExecutableElement method) {
    System.out.println(method);

    System.out.println(method.getParameters());
  }

//  TODO: get metadata
//  protected abstract
}
