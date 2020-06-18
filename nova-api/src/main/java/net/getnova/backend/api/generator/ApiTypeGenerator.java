package net.getnova.backend.api.generator;

import graphql.schema.GraphQLObjectType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiType;
import net.getnova.backend.api.data.FieldData;
import net.getnova.backend.api.data.TypeData;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public final class ApiTypeGenerator {

    private ApiTypeGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLObjectType generateGraphQLType(final TypeData typeData) {
        final GraphQLObjectType.Builder type = GraphQLObjectType.newObject()
                .name(typeData.getName())
                .description(typeData.getDescription());

        for (final FieldData field : typeData.getFields()) type.field(ApiFieldGenerator.generateGraphQLField(field));
        return type.build();
    }

    public static Set<TypeData> generateTypes(final Set<Class<?>> classes) {
        final Set<TypeData> types = new LinkedHashSet<>();
        for (final Class<?> clazz : classes) {
            final TypeData type = generateType(clazz);
            if (type != null) types.add(type);
        }
        return types;
    }

    private static TypeData generateType(final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ApiType.class)) return null;
        final ApiType type = clazz.getAnnotation(ApiType.class);

        return new TypeData(type.name(),
                String.join("\n", type.description()),
                ApiFieldGenerator.generateFields(clazz),
                clazz);
    }
}
