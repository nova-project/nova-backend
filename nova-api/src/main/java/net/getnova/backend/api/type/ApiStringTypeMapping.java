package net.getnova.backend.api.type;

import graphql.Scalars;

public class ApiStringTypeMapping extends ApiTypeMapping {

    public ApiStringTypeMapping() {
        super(Scalars.GraphQLString, String.class);
    }
}
