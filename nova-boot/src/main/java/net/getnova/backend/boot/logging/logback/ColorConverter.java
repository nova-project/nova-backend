package net.getnova.backend.boot.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import net.getnova.backend.boot.ansi.AnsiColor;
import net.getnova.backend.boot.ansi.AnsiElement;
import net.getnova.backend.boot.ansi.AnsiStyle;

public final class ColorConverter extends CompositeConverter<ILoggingEvent> {

  private static final String START = ANSIConstants.ESC_START + AnsiStyle.BOLD + ";";
  private static final String RESET = ANSIConstants.ESC_START + AnsiStyle.RESET + ANSIConstants.ESC_END;

  @Override
  protected String transform(final ILoggingEvent event, final String in) {
    final AnsiElement color = switch (event.getLevel().toInt()) {
      case Level.ERROR_INT -> AnsiColor.RED;
      case Level.WARN_INT -> AnsiColor.YELLOW;
      case Level.INFO_INT -> AnsiColor.BLUE;
      default -> AnsiColor.GREEN;
    };

    return START + color + ANSIConstants.ESC_END + in + RESET;
  }
}
