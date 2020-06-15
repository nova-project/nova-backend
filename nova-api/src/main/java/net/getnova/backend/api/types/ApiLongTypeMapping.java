package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiLongTypeMapping extends ApiTypeMapping {

    public ApiLongTypeMapping() {
        super(Scalars.GraphQLLong, long.class, Long.class);
    }
}
