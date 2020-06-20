package net.getnova.backend.api.type;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.Base64;

public class ApiByteArrayMapping extends ApiTypeMapping implements Coercing<byte[], String> {

    public ApiByteArrayMapping() {
        super("Base64", "Binary data formatted as base64.", byte[].class);
    }

    @Override
    public String serialize(final Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof byte[]) {
            return Base64.getEncoder().encodeToString((byte[]) dataFetcherResult);
        } else if (dataFetcherResult instanceof String) {
            return (String) dataFetcherResult;
        } else {
            throw ApiTypeUtils.serializationException(dataFetcherResult, String.class, byte[].class);
        }
    }

    @Override
    public byte[] parseValue(final Object input) throws CoercingParseValueException {
        if (input instanceof String) {
            try {
                return Base64.getDecoder().decode((String) input);
            } catch (IllegalArgumentException e) {
                throw new CoercingParseValueException("Input string \"" + input + "\" is not a valid Base64 value", e);
            }
        }
        if (input instanceof byte[]) {
            return (byte[]) input;
        }
        throw ApiTypeUtils.valueParsingException(input, String.class, byte[].class);
    }

    @Override
    public byte[] parseLiteral(final Object input) throws CoercingParseLiteralException {
        StringValue string = ApiTypeUtils.literalOrException(input, StringValue.class);
        try {
            return Base64.getDecoder().decode(string.getValue());
        } catch (IllegalArgumentException e) {
            throw new CoercingParseLiteralException("Input string \"" + input + "\" is not a valid Base64 value", e);
        }
    }
}
