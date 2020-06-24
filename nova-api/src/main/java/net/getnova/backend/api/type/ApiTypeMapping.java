package net.getnova.backend.api.type;

import graphql.schema.Coercing;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLScalarType;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ApiTypeMapping {

    @Getter
    private final GraphQLScalarType graphQlScalar;
    @Getter(AccessLevel.PACKAGE)
    private final Set<Class<?>> types;
    private GraphQLList listType;

    public ApiTypeMapping(final String name, final String description, final Class<?>... types) {
        if (this instanceof Coercing) {
            this.graphQlScalar = GraphQLScalarType.newScalar().name(name).description(description).coercing((Coercing<?, ?>) this).build();
            this.types = new LinkedHashSet<>(Arrays.asList(types));
        } else throw new RuntimeException(this.getClass().getName() + " is not a " + Coercing.class.getName() + ".");
    }

    public ApiTypeMapping(final GraphQLScalarType graphQlScalar, final Class<?>... types) {
        this.graphQlScalar = graphQlScalar;
        this.types = new LinkedHashSet<>(Arrays.asList(types));
    }

    public final GraphQLList getListType() {
        if (this.listType == null) this.listType = GraphQLList.list(this.getGraphQlScalar());
        return this.listType;
    }
}
