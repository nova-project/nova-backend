package net.getnova.backend.api.generator;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiMutation;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.MutationData;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public final class ApiMutationGenerator {

    private ApiMutationGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLObjectType generateGraphQLMutation(final Set<MutationData> queries) {
        if (queries.isEmpty()) return null;

        final GraphQLObjectType.Builder mutation = GraphQLObjectType.newObject()
                .name("MutationType");

        GraphQLFieldDefinition.Builder fieldDefinition;
        for (final MutationData mutationData : queries) {
            fieldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                    .name(mutationData.getName())
                    .description(mutationData.getDescription());

            final GraphQLOutputType type = Scalars.GraphQLString;
            fieldDefinition.type(mutationData.isNullable() ? type : GraphQLNonNull.nonNull(type)); // TODO: Type

            if (!mutationData.getDeprecationReason().isEmpty())
                fieldDefinition.deprecate(mutationData.getDeprecationReason());

            for (final ArgumentData argument : mutationData.getArguments())
                fieldDefinition.argument(ApiArgumentGenerator.generateGraphQLArgument(argument));

            mutation.field(fieldDefinition.build());
        }

        return mutation.build();
    }

    public static Set<MutationData> generateMutations(final Set<Object> mutationsInstances) {
        final Set<MutationData> queries = new LinkedHashSet<>();

        Class<?> clazz;
        for (final Object instance : mutationsInstances) {
            clazz = instance.getClass();
            for (final Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                final MutationData mutationData = generateMutation(clazz, instance, method);
                if (mutationData != null) queries.add(mutationData);
                method.setAccessible(false);
            }
        }

        return queries;
    }

    private static MutationData generateMutation(final Class<?> clazz, final Object instance, final Method method) {
        if (!method.isAnnotationPresent(ApiMutation.class)) return null;
        final ApiMutation mutationAnnotation = method.getAnnotation(ApiMutation.class);

        return new MutationData(mutationAnnotation.name(),
                String.join("\n", mutationAnnotation.description()),
                String.join("\n", mutationAnnotation.deprecationReason()),
                mutationAnnotation.nullable(),
                method.getReturnType(),
                clazz, instance, ApiArgumentGenerator.generateArguments(clazz, method));
    }
}
