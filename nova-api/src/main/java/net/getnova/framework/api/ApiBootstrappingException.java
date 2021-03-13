package net.getnova.framework.api;

/**
 * An {@link RuntimeException} witch indicated that an error occurred in the process of bootstrapping the api system.
 *
 * @see net.getnova.framework.api.parser.ApiParser#parse(Collection)
 */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class ApiBootstrappingException extends RuntimeException {

  /**
   * Creates a new {@link ApiBootstrappingException} with the specified message.
   *
   * @param message the message for the {@link Throwable}.
   * @see Throwable#getMessage()
   */
  public ApiBootstrappingException(final String message) {
    super(message);
  }

  /**
   * Creates a new {@link ApiBootstrappingException} with the specified message and a parent {@link Throwable}.
   *
   * @param message the message for the {@link Throwable}.
   * @param cause   the parent {@link Throwable}.
   * @see Throwable#getMessage()
   * @see Throwable#getCause()
   */
  public ApiBootstrappingException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
