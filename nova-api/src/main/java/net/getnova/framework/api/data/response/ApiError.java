package net.getnova.framework.api.data.response;

import java.util.Collection;
import javax.validation.ConstraintViolation;

public interface ApiError {

  static ApiError of(final String target, final String message) {
    return new CustomApiError(target, message);
  }

  static ApiError of(final ConstraintViolation<?> violation) {
    return new ViolationApiError(violation);
  }

  static ApiError[] of(final Collection<? extends ConstraintViolation<?>> violation) {
    return violation.stream()
      .map(ApiError::of)
      .toArray(ApiError[]::new);
  }

  String getTarget();

  String getMessage();
}
