package net.getnova.framework.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

  private final String field;
  private final String expected;

  public ValidationException(final String field, final String expected, final Throwable cause) {
    super(cause);
    this.field = field;
    this.expected = expected;
  }

  @Override
  public String getMessage() {
    return String.format("Field: \"%s\", Expected: \"%s\"", this.field, this.expected);
  }
}
