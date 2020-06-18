package net.getnova.backend.api.generator;

import graphql.schema.GraphQLSchema;
import net.getnova.backend.api.data.MutationData;
import net.getnova.backend.api.data.QueryData;
import net.getnova.backend.api.data.TypeData;

import java.util.Set;

public final class ApiSchemaGenerator {

    private ApiSchemaGenerator() {
        throw new UnsupportedOperationException();
    }

    public static GraphQLSchema generateGraphQLSchema(
            final Set<TypeData> types,
            final Set<QueryData<?>> queries,
            final Set<MutationData<?>> mutations
    ) {
        final GraphQLSchema.Builder schema = GraphQLSchema.newSchema();

        for (final TypeData type : types) schema.additionalType(ApiTypeGenerator.generateGraphQLType(type));
        if (!queries.isEmpty()) schema.query(ApiQueryGenerator.generateGraphQLQuery(queries));
        if (!mutations.isEmpty()) schema.mutation(ApiMutationGenerator.generateGraphQLMutation(mutations));
        return schema.build();
    }
}
