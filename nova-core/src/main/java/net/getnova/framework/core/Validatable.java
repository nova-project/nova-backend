package net.getnova.framework.core;

import net.getnova.framework.core.exception.ValidationException;

public interface Validatable {

  void validate() throws ValidationException;
}
