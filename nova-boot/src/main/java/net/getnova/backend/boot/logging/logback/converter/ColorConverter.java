package net.getnova.backend.boot.logging.logback.converter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import net.getnova.backend.boot.logging.logback.converter.ansi.AnsiColor;
import net.getnova.backend.boot.logging.logback.converter.ansi.AnsiElement;
import net.getnova.backend.boot.logging.logback.converter.ansi.AnsiStyle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ColorConverter extends CompositeConverter<ILoggingEvent> {

  private static final String ENCODE_START = "\033[";
  private static final String ENCODE_END = "m";
  private static final String ENCODE_JOIN = ";";
  private static final Map<String, AnsiElement> ELEMENTS;
  private static final Map<Level, AnsiElement> LEVELS;

  static {
    final Map<String, AnsiElement> localElements = new HashMap<>();
    for (final AnsiColor element : AnsiColor.values()) localElements.put(element.name().toLowerCase(), element);
    for (final AnsiStyle element : AnsiStyle.values()) localElements.put(element.name().toLowerCase(), element);
    ELEMENTS = Collections.unmodifiableMap(localElements);

    LEVELS = Map.of(
      Level.ERROR, AnsiColor.RED,
      Level.WARN, AnsiColor.YELLOW,
      Level.INFO, AnsiColor.BLUE,
      Level.DEBUG, AnsiColor.GREEN,
      Level.TRACE, AnsiColor.GREEN
    );
  }

  @Override
  protected String transform(final ILoggingEvent event, final String in) {
    final StringBuilder builder = new StringBuilder();

    if (this.getOptionList() == null) {
      builder.append(ENCODE_START).append(AnsiStyle.BOLD).append(ENCODE_END)
        .append(ENCODE_START).append(LEVELS.get(event.getLevel())).append(ENCODE_END);
    } else {
      boolean first = true;
      for (final String element : this.getOptionList()) {
        AnsiElement ansiElement = ELEMENTS.get(element);
        if (ansiElement != null) {
          if (!first) builder.append(ENCODE_JOIN);
          builder.append(ENCODE_START).append(ansiElement).append(ENCODE_END);
          first = false;
        }
      }
    }

    return builder.append(in)
      .append(ENCODE_START).append(AnsiStyle.RESET).append(ENCODE_END)
      .toString();
  }
}
