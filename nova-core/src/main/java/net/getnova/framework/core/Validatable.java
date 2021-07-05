package net.getnova.framework.core;

public interface Validatable {

  static void validate(final Object object) throws ValidationException {
    if (object instanceof Validatable) {
      ((Validatable) object).validate();
    }
  }

  void validate() throws ValidationException;
}
