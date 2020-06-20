package net.getnova.backend.api.generator;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiQuery;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.QueryData;
import net.getnova.backend.api.execution.ApiQueryDataFetcher;
import net.getnova.backend.api.type.ApiStringTypeMapping;
import net.getnova.backend.api.type.ApiTypeMapping;
import net.getnova.backend.api.type.ApiTypeMappingHandler;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public final class ApiQueryGenerator {

    private ApiQueryGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLObjectType generateGraphQLQuery(final Set<QueryData<?>> queries) {
        if (queries.isEmpty()) return null;

        final GraphQLObjectType.Builder query = GraphQLObjectType.newObject()
                .name("QueryType");

        GraphQLFieldDefinition.Builder fieldDefinition;
        for (final QueryData<?> queryData : queries) {
            fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                    .name(queryData.getName())
                    .description(queryData.getDescription())
                    .dataFetcher(new ApiQueryDataFetcher<>(queryData));

            ApiTypeMapping type = ApiTypeMappingHandler.INSTANCE.getType(queryData.getType());
            if (type == null) {
                log.error("Unable to find graphql type mapping for {}.", queryData.getType().getName());
                type = new ApiStringTypeMapping();
            }

            fieldDefinition.type(queryData.isNullable() ? type.getGraphQlScalar() : GraphQLNonNull.nonNull(type.getGraphQlScalar()));
            if (!queryData.getDeprecationReason().isEmpty())
                fieldDefinition.deprecate(queryData.getDeprecationReason());

            for (final ArgumentData argument : queryData.getArguments())
                fieldDefinition.argument(ApiArgumentGenerator.generateGraphQLArgument(argument));

            query.field(fieldDefinition.build());
        }

        return query.build();
    }

    public static Set<QueryData<?>> generateQueries(final Set<Object> queriesInstances) {
        final Set<QueryData<?>> queries = new LinkedHashSet<>();

        Class<?> clazz;
        for (final Object instance : queriesInstances) {
            clazz = instance.getClass();
            for (final Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                final QueryData<?> queryData = generateQuery(clazz, instance, method);
                if (queryData != null) queries.add(queryData);
                method.setAccessible(false);
            }
        }

        return queries;
    }

    private static <T> QueryData<T> generateQuery(final Class<?> clazz, final Object instance, final Method method) {
        if (!method.isAnnotationPresent(ApiQuery.class)) return null;
        final ApiQuery queryAnnotation = method.getAnnotation(ApiQuery.class);

        return new QueryData<>(queryAnnotation.name(),
                String.join("\n", queryAnnotation.description()),
                String.join("\n", queryAnnotation.deprecationReason()),
                queryAnnotation.nullable(),
                (Class<T>) method.getReturnType(),
                clazz, method, instance, ApiArgumentGenerator.generateArguments(clazz, method));
    }
}
