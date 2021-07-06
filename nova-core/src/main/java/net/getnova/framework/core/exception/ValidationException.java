package net.getnova.framework.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationException extends RuntimeException {

  private final String field;
  private final String error;

  public ValidationException(final String field, final String error, final Throwable cause) {
    super(cause);
    this.field = field;
    this.error = error;
  }

  @Override
  public String getMessage() {
    return String.format("Field: \"%s\", Error: \"%s\"", this.field, this.error);
  }
}
