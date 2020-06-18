package net.getnova.backend.api.generator;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiMutation;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.MutationData;
import net.getnova.backend.api.execution.ApiMutationDataFetcher;
import net.getnova.backend.api.types.ApiStringTypeMapping;
import net.getnova.backend.api.types.ApiTypeMapping;
import net.getnova.backend.api.types.ApiTypeMappingHandler;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public final class ApiMutationGenerator {

    private ApiMutationGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLObjectType generateGraphQLMutation(final Set<MutationData<?>> queries) {
        if (queries.isEmpty()) return null;

        final GraphQLObjectType.Builder mutation = GraphQLObjectType.newObject()
                .name("MutationType");

        GraphQLFieldDefinition.Builder fieldDefinition;
        for (final MutationData<?> mutationData : queries) {
            fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                    .name(mutationData.getName())
                    .description(mutationData.getDescription())
                    .dataFetcher(new ApiMutationDataFetcher<>(mutationData));

            ApiTypeMapping type = ApiTypeMappingHandler.INSTANCE.getType(mutationData.getType());
            if (type == null) {
                log.error("Unable to find graphql type mapping for {}.", mutationData.getType().getName());
                type = new ApiStringTypeMapping();
            }

            fieldDefinition.type(mutationData.isNullable() ? type.getGraphQlScalar() : GraphQLNonNull.nonNull(type.getGraphQlScalar()));
            if (!mutationData.getDeprecationReason().isEmpty())
                fieldDefinition.deprecate(mutationData.getDeprecationReason());

            for (final ArgumentData argument : mutationData.getArguments())
                fieldDefinition.argument(ApiArgumentGenerator.generateGraphQLArgument(argument));

            mutation.field(fieldDefinition.build());
        }

        return mutation.build();
    }

    public static Set<MutationData<?>> generateMutations(final Set<Object> mutationsInstances) {
        final Set<MutationData<?>> queries = new LinkedHashSet<>();

        Class<?> clazz;
        for (final Object instance : mutationsInstances) {
            clazz = instance.getClass();
            for (final Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                final MutationData<?> mutationData = generateMutation(clazz, instance, method);
                if (mutationData != null) queries.add(mutationData);
                method.setAccessible(false);
            }
        }

        return queries;
    }

    private static <T> MutationData<T> generateMutation(final Class<?> clazz, final Object instance, final Method method) {
        if (!method.isAnnotationPresent(ApiMutation.class)) return null;
        final ApiMutation mutationAnnotation = method.getAnnotation(ApiMutation.class);

        return new MutationData<>(mutationAnnotation.name(),
                String.join("\n", mutationAnnotation.description()),
                String.join("\n", mutationAnnotation.deprecationReason()),
                mutationAnnotation.nullable(),
                (Class<T>) method.getReturnType(),
                clazz, method, instance, ApiArgumentGenerator.generateArguments(clazz, method));
    }
}
