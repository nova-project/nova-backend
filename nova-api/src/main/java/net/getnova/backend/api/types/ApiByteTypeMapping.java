package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiByteTypeMapping extends ApiTypeMapping {

    public ApiByteTypeMapping() {
        super(Scalars.GraphQLByte, byte.class, Byte.class);
    }
}
