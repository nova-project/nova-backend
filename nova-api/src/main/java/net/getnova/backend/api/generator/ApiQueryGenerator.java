package net.getnova.backend.api.generator;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiQuery;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.QueryData;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public final class ApiQueryGenerator {

    private ApiQueryGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLObjectType generateGraphQLQuery(final Set<QueryData> queries) {
        if (queries.isEmpty()) return null;

        final GraphQLObjectType.Builder query = GraphQLObjectType.newObject()
                .name("QueryType");

        GraphQLFieldDefinition.Builder fieldDefinition;
        for (final QueryData queryData : queries) {
            fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                    .name(queryData.getName())
                    .description(queryData.getDescription());

            final GraphQLOutputType type = Scalars.GraphQLString;
            fieldDefinition.type(queryData.isNullable() ? type : GraphQLNonNull.nonNull(type)); // TODO: Type

            if (!queryData.getDeprecationReason().isEmpty())
                fieldDefinition.deprecate(queryData.getDeprecationReason());

            for (final ArgumentData argument : queryData.getArguments())
                fieldDefinition.argument(ApiArgumentGenerator.generateGraphQLArgument(argument));

            query.field(fieldDefinition.build());
        }

        return query.build();
    }

    public static Set<QueryData> generateQueries(final Set<Object> queriesInstances) {
        final Set<QueryData> queries = new LinkedHashSet<>();

        Class<?> clazz;
        for (final Object instance : queriesInstances) {
            clazz = instance.getClass();
            for (final Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                final QueryData queryData = generateQuery(clazz, instance, method);
                if (queryData != null) queries.add(queryData);
                method.setAccessible(false);
            }
        }

        return queries;
    }

    private static QueryData generateQuery(final Class<?> clazz, final Object instance, final Method method) {
        if (!method.isAnnotationPresent(ApiQuery.class)) return null;
        final ApiQuery queryAnnotation = method.getAnnotation(ApiQuery.class);

        return new QueryData(queryAnnotation.name(),
                String.join("\n", queryAnnotation.description()),
                String.join("\n", queryAnnotation.deprecationReason()),
                queryAnnotation.nullable(),
                method.getReturnType(),
                clazz, instance, ApiArgumentGenerator.generateArguments(clazz, method));
    }
}
