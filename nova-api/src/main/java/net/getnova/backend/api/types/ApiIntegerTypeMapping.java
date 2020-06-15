package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiIntegerTypeMapping extends ApiTypeMapping {

    public ApiIntegerTypeMapping() {
        super(Scalars.GraphQLInt, int.class, Integer.class);
    }
}
