package net.getnova.backend.api.generator;

import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLNonNull;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiArgument;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.type.ApiStringTypeMapping;
import net.getnova.backend.api.type.ApiTypeMapping;
import net.getnova.backend.api.type.ApiTypeMappingHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
final class ApiArgumentGenerator {

    private ApiArgumentGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLArgument generateGraphQLArgument(final ArgumentData argumentData) {
        ApiTypeMapping type = ApiTypeMappingHandler.INSTANCE.getType(argumentData.getType());
        if (type == null) {
            log.error("Unable to find graphql type mapping for {}.", argumentData.getType().getName());
            type = new ApiStringTypeMapping();
        }

        return GraphQLArgument.newArgument()
                .name(argumentData.getName())
                .description(argumentData.getDescription())
                .type(argumentData.isNullable() ? type.getGraphQlScalar() : GraphQLNonNull.nonNull(type.getGraphQlScalar()))
                .build();
    }

    static Set<ArgumentData> generateArguments(final Class<?> clazz, final Method method) {
        final Set<ArgumentData> arguments = new LinkedHashSet<>();

        ArgumentData argument;
        for (final Parameter parameter : method.getParameters()) {
            argument = generateArgument(clazz, method, parameter);
            if (argument != null) arguments.add(argument);
        }

        return arguments;
    }

    static ArgumentData generateArgument(final Class<?> clazz, final Method method, final Parameter parameter) {
        if (!parameter.isAnnotationPresent(ApiArgument.class)) {
            log.error("Api argument {}.{}.{} does not has annotation {}.", clazz.getName(), method.getName(), parameter.getName(), ApiArgument.class.getName());
            return null;
        }

        final ApiArgument queryArgumentAnnotation = parameter.getAnnotation(ApiArgument.class);

        return new ArgumentData(queryArgumentAnnotation.name(),
                String.join("\n", queryArgumentAnnotation.description()),
                queryArgumentAnnotation.nullable(),
                parameter.getType(),
                clazz, method, parameter);
    }
}
