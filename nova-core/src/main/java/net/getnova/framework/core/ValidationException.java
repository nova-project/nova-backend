package net.getnova.framework.core;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

  private final String field;
  private final String error;

  @Override
  public String getMessage() {
    return String.format("Field: \"%s\", Error: \"%s\"", this.field, this.error);
  }
}
