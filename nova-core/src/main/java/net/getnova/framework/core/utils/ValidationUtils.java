package net.getnova.framework.core.utils;

import net.getnova.framework.core.Validatable;
import net.getnova.framework.core.exception.ValidationException;

public final class ValidationUtils {

  private ValidationUtils() {
    throw new UnsupportedOperationException();
  }

  public static void validate(final Object object) throws ValidationException {
    if (object instanceof Validatable) {
      ((Validatable) object).validate();
    }
  }

  public static void validate(final Validatable validatable) throws ValidationException {
    validatable.validate();
  }
}
