package net.getnova.backend.api.types;

import graphql.Scalars;

import java.util.UUID;

public class ApiUUIDTypeMapping extends ApiTypeMapping {

    public ApiUUIDTypeMapping() {
        super(Scalars.GraphQLID, UUID.class);
    }
}
