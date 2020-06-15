package net.getnova.backend.api.types;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public abstract class ApiTypeMapping {

    private final GraphQLScalarType graphQlScalar;
    private final Set<Class<?>> types;
    private final boolean additional;

    public ApiTypeMapping(final String name, final String description, final Class<?>... types) {
        if (this instanceof Coercing) {
            this.graphQlScalar = GraphQLScalarType.newScalar().name(name).description(description).coercing((Coercing<?, ?>) this).build();
            this.types = new LinkedHashSet<>(Arrays.asList(types));
            this.additional = true;
        } else throw new RuntimeException(this.getClass().getName() + " is not a " + Coercing.class.getName() + ".");
    }

    public ApiTypeMapping(final GraphQLScalarType graphQlScalar, final Class<?>... types) {
        this.graphQlScalar = graphQlScalar;
        this.types = new LinkedHashSet<>(Arrays.asList(types));
        this.additional = false;
    }
}
