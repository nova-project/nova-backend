package net.getnova.framework.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

  public NotFoundException() {
    super(HttpStatus.NOT_FOUND);
  }

  public NotFoundException(final String reason) {
    super(HttpStatus.NOT_FOUND, reason);
  }

  public NotFoundException(final String reason, final Throwable cause) {
    super(HttpStatus.NOT_FOUND, reason, cause);
  }
}
