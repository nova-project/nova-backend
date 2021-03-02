package net.getnova.framework.api.exception;

public class ParserApiException extends ApiException {

  public ParserApiException(final String message) {
    super(message);
  }

  public ParserApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ParserApiException(final Throwable cause) {
    super(cause);
  }
}
