package net.getnova.framework.api;

@SuppressWarnings("PMD.MissingSerialVersionUID")
public class ApiBootstrappingException extends RuntimeException {

  public ApiBootstrappingException(final String message) {
    super(message);
  }

  public ApiBootstrappingException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
