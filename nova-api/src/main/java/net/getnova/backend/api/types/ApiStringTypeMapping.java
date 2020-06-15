package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiStringTypeMapping extends ApiTypeMapping {

    public ApiStringTypeMapping() {
        super(Scalars.GraphQLString, String.class);
    }
}
