package net.getnova.backend.api.type;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class ApiZonedDateTimeMapping extends ApiTypeMapping implements Coercing<ZonedDateTime, Long> {

    public ApiZonedDateTimeMapping() {
        super("Timestamp", "a timestamp", ZonedDateTime.class);
    }

    private static String typeName(final Object input) {
        if (input == null) {
            return "null";
        }

        return input.getClass().getSimpleName();
    }

    private static Long convertImpl(final Object input) {
        if (input instanceof ZonedDateTime) {
            return ((ZonedDateTime) input).toInstant().toEpochMilli();
        } else if (input instanceof Long) {
            return (Long) input;
        } else if (input instanceof Number || input instanceof String) {
            BigDecimal value;
            try {
                value = new BigDecimal(input.toString());
            } catch (NumberFormatException e) {
                return null;
            }
            try {
                return value.longValueExact();
            } catch (ArithmeticException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Long serialize(final Object dataFetcherResult) throws CoercingSerializeException {
        final Long result = convertImpl(dataFetcherResult);
        if (result == null)
            throw new CoercingSerializeException("Unable to convert type " + typeName(dataFetcherResult) + " into a " + typeName(Long.class) + ".");
        return result;
    }

    @Override
    public ZonedDateTime parseValue(final Object input) throws CoercingParseValueException {
        final Long ms = convertImpl(input);
        if (ms == null)
            throw new CoercingSerializeException("Unable to convert type " + typeName(input) + " into a " + typeName(ZonedDateTime.class) + ".");
        return Instant.ofEpochMilli(ms).atZone(ZoneOffset.UTC);
    }

    @Override
    public ZonedDateTime parseLiteral(final Object input) throws CoercingParseLiteralException {
        final Long ms = convertImpl(input);
        if (ms == null)
            throw new CoercingSerializeException("Unable to convert type " + typeName(input) + " into a " + typeName(ZonedDateTime.class) + ".");
        return Instant.ofEpochMilli(ms).atZone(ZoneOffset.UTC);
    }
}
