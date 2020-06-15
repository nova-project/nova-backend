package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiShortTypeMapping extends ApiTypeMapping {

    public ApiShortTypeMapping() {
        super(Scalars.GraphQLShort, short.class, Short.class);
    }
}
