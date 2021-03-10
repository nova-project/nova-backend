package net.getnova.framework.api.exception;

import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.Data;

@Data
public class ValidationApiException extends ApiException {

  private final Set<? extends ConstraintViolation<?>> violations;
}
