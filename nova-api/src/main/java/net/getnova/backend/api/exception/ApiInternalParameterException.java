package net.getnova.backend.api.exception;

public class ApiInternalParameterException extends ApiParameterException {

  public ApiInternalParameterException(final String message) {
    super(message);
  }

  public ApiInternalParameterException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
