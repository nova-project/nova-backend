package net.getnova.backend.api.type;

import graphql.language.Value;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.Arrays;
import java.util.stream.Collectors;

final class ApiTypeUtils {

    private ApiTypeUtils() {
        throw new UnsupportedOperationException();
    }

    static <T extends Value<?>> T literalOrException(final Object input, final Class<T> valueType) {
        if (valueType.isInstance(input)) {
            return valueType.cast(input);
        }
        throw new CoercingParseLiteralException(errorMessage(input, valueType));
    }

    static String errorMessage(final Object input, final Class<?>... allowedTypes) {
        String types = Arrays.stream(allowedTypes)
                .map(type -> "'" + type.getSimpleName() + "'")
                .collect(Collectors.joining(" or "));
        return String.format("Expected %stype %s but was '%s'", input instanceof Value ? "AST " : "",
                types, input == null ? "null" : input.getClass().getSimpleName());
    }

    static CoercingParseValueException valueParsingException(final Object input, final Class<?>... allowedTypes) {
        return new CoercingParseValueException(errorMessage(input, allowedTypes));
    }

    static CoercingSerializeException serializationException(final Object input, final Class<?>... allowedTypes) {
        return new CoercingSerializeException(errorMessage(input, allowedTypes));
    }
}
