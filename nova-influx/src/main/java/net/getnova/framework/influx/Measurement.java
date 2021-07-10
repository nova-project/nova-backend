package net.getnova.framework.influx;

import java.time.Instant;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
public class Measurement {

  private static final String BACK_SLASH = "\\";
  private static final String ESCAPED_BACK_SLASH = BACK_SLASH + BACK_SLASH;

  private static final String COMMA = ",";
  private static final String ESCAPED_COMMA = BACK_SLASH + COMMA;

  private static final String EQUALS = "=";
  private static final String ESCAPED_EQUALS = BACK_SLASH + EQUALS;

  private static final String SPACE = " ";
  private static final String ESCAPED_SPACE = BACK_SLASH + SPACE;

  private static final String DOUBLE_QUOTE = "\"";
  private static final String ESCAPED_DOUBLE_QUOTE = BACK_SLASH + DOUBLE_QUOTE;

  private final String name;
  private final Map<String, String> tags;
  private final Field field;
  private final Instant timestamp;

  public String toLineProtocol(final WritePrecision precision) {
    final StringBuilder builder = new StringBuilder();

    builder.append(
      this.name.replace(COMMA, ESCAPED_COMMA).replace(SPACE, ESCAPED_SPACE)
    );

    this.tags.forEach((key, value) -> {
      builder.append(COMMA)
        .append(key.replace(COMMA, ESCAPED_COMMA).replace(EQUALS, ESCAPED_EQUALS).replace(SPACE, ESCAPED_SPACE))
        .append(EQUALS)
        .append(value.replace(COMMA, ESCAPED_COMMA).replace(EQUALS, ESCAPED_EQUALS).replace(SPACE, ESCAPED_SPACE));
    });

    builder.append(SPACE);

    this.field.toLineProtocol(builder);

    builder.append(SPACE);

    precision.getAppendTimestamp()
      .accept(builder, this.timestamp);

    return builder.toString();
  }

  @Data
  protected static abstract class Field {

    private final String key;

    public void toLineProtocol(final StringBuilder builder) {
      builder
        .append(this.key.replace(COMMA, ESCAPED_COMMA).replace(EQUALS, ESCAPED_EQUALS).replace(SPACE, ESCAPED_SPACE))
        .append(EQUALS);
      this.valueToLineProtocol(builder);
    }

    protected abstract void valueToLineProtocol(StringBuilder builder);
  }

  @Getter
  @ToString
  @EqualsAndHashCode
  public static final class DoubleField extends Field {

    private final double value;

    public DoubleField(final String key, final double value) {
      super(key);
      this.value = value;
    }

    @Override
    protected void valueToLineProtocol(final StringBuilder builder) {
      builder.append(this.value);
    }
  }

  @Getter
  @ToString
  @EqualsAndHashCode
  public static final class LongField extends Field {

    private final long value;

    public LongField(final String key, final long value) {
      super(key);
      this.value = value;
    }

    @Override
    protected void valueToLineProtocol(final StringBuilder builder) {
      builder.append(this.value);
    }
  }

  // TODO: unsigned long

  @Getter
  @ToString
  @EqualsAndHashCode
  public static final class StringField extends Field {

    private final String value;

    public StringField(final String key, final String value) {
      super(key);
      this.value = value;
    }

    @Override
    protected void valueToLineProtocol(final StringBuilder builder) {
      builder.append(DOUBLE_QUOTE)
        .append(this.value.replace(BACK_SLASH, ESCAPED_BACK_SLASH).replace(DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE))
        .append(DOUBLE_QUOTE);
    }
  }

  @Getter
  @ToString
  @EqualsAndHashCode
  public static final class BooleanField extends Field {

    private static final char T = 't';
    private static final char F = 'f';

    private final boolean value;

    public BooleanField(final String key, final boolean value) {
      super(key);
      this.value = value;
    }

    @Override
    protected void valueToLineProtocol(final StringBuilder builder) {
      builder.append(this.value ? T : F);
    }
  }
}
