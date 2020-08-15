package net.getnova.backend.service;

import java.io.IOException;

class ServiceException extends IOException {

  ServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
