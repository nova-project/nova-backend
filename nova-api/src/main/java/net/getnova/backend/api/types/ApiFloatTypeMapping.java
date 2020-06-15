package net.getnova.backend.api.types;

import graphql.Scalars;

public class ApiFloatTypeMapping extends ApiTypeMapping {

    public ApiFloatTypeMapping() {
        super(Scalars.GraphQLFloat, float.class, Float.class);
    }
}
