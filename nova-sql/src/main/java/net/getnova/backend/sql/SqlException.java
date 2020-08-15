package net.getnova.backend.sql;

import java.io.IOException;

class SqlException extends IOException {

  SqlException(final String message) {
    super(message);
  }

  SqlException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
