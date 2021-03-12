package net.getnova.framework.api.data.response;

import javax.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ViolationApiError implements ApiError {

  private final ConstraintViolation<?> violation;

  @Override
  public String getTarget() {
    return this.violation.getPropertyPath().toString();
  }

  @Override
  public String getMessage() {
    return this.violation.getMessage();
  }
}
