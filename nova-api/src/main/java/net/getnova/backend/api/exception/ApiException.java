package net.getnova.backend.api.exception;

import java.io.IOException;

public class ApiException extends IOException {

  public ApiException(final String message) {
    super(message);
  }

  public ApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
