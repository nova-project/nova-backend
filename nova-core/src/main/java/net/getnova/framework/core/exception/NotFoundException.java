package net.getnova.framework.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotFoundException extends RuntimeException {

  private final String component;

  public NotFoundException(final String component, final Throwable cause) {
    super(cause);
    this.component = component;
  }

  @Override
  public String getMessage() {
    return String.format("Component: \"%s\"", this.component);
  }
}
