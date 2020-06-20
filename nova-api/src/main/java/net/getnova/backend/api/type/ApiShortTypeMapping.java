package net.getnova.backend.api.type;

import graphql.Scalars;

public class ApiShortTypeMapping extends ApiTypeMapping {

    public ApiShortTypeMapping() {
        super(Scalars.GraphQLShort, short.class, Short.class);
    }
}
