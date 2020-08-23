package net.getnova.backend.boot.logging.logback.converter;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;

public final class ExtendedWhitespaceThrowableProxyConverter extends ExtendedThrowableProxyConverter {

  @Override
  protected String throwableProxyToString(final IThrowableProxy throwableProxy) {
    return CoreConstants.LINE_SEPARATOR + super.throwableProxyToString(throwableProxy) + CoreConstants.LINE_SEPARATOR;
  }
}
