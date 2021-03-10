package net.getnova.framework.api.exception;

public class ApiException extends Exception {

  public ApiException() {
  }

  public ApiException(final String message) {
    super(message);
  }

  public ApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ApiException(final Throwable cause) {
    super(cause);
  }
}
