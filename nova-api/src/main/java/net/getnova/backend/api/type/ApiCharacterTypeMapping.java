package net.getnova.backend.api.type;

import graphql.Scalars;

public class ApiCharacterTypeMapping extends ApiTypeMapping {

    public ApiCharacterTypeMapping() {
        super(Scalars.GraphQLChar, char.class, Character.class);
    }
}
