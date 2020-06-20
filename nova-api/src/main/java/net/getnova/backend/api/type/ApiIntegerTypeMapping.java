package net.getnova.backend.api.type;

import graphql.Scalars;

public class ApiIntegerTypeMapping extends ApiTypeMapping {

    public ApiIntegerTypeMapping() {
        super(Scalars.GraphQLInt, int.class, Integer.class);
    }
}
