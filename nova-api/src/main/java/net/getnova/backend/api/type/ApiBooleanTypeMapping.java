package net.getnova.backend.api.type;

import graphql.Scalars;

public class ApiBooleanTypeMapping extends ApiTypeMapping {

    public ApiBooleanTypeMapping() {
        super(Scalars.GraphQLBoolean, boolean.class, Boolean.class);
    }
}
