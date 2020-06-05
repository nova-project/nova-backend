package net.getnova.backend.api;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiField;
import net.getnova.backend.api.annotations.ApiSupperType;
import net.getnova.backend.api.annotations.ApiType;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
final class ApiParser {

    private ApiParser() {
        throw new UnsupportedOperationException();
    }

    static GraphQLSchema parse(final Class<?>... classes) {
        return GraphQLSchema.newSchema().query(query(classes)).build();
    }

    private static GraphQLObjectType query(final Class<?>... classes) {
        final GraphQLObjectType.Builder queryBuilder = GraphQLObjectType.newObject()
                .name("QueryType");

        for (final Class<?> clazz : classes) {
            final Map.Entry<String, GraphQLObjectType> type = parseClass(clazz);
            if (type != null) queryBuilder.field(typeField(type.getKey(), GraphQLList.list(type.getValue())));
        }

        return queryBuilder.build();
    }

    private static GraphQLFieldDefinition typeField(final String name, final GraphQLOutputType type) {
        return GraphQLFieldDefinition.newFieldDefinition().name(name).type(type).build();
    }

    private static Map.Entry<String, GraphQLObjectType> parseClass(final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ApiType.class)) return null;
        final ApiType type = clazz.getAnnotation(ApiType.class);

        final GraphQLObjectType.Builder builder = GraphQLObjectType.newObject()
                .name(type.name())
                .description(String.join("\n", type.description()));

        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass.isAnnotationPresent(ApiSupperType.class)) {
            builder.fields(parseSingleClass(superclass));
        }
        builder.fields(parseSingleClass(clazz));

        return new AbstractMap.SimpleEntry<>(type.queryName(), builder.build());
    }

    private static List<GraphQLFieldDefinition> parseSingleClass(final Class<?> clazz) {
        final List<GraphQLFieldDefinition> fields = new LinkedList<>();
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ApiField.class)) fields.add(parseField(field));
            field.setAccessible(false);
        }
        return fields;
    }

    private static GraphQLFieldDefinition parseField(final Field field) {
        final ApiField apiField = field.getAnnotation(ApiField.class);

        final GraphQLOutputType type = Scalars.GraphQLString;

        return GraphQLFieldDefinition.newFieldDefinition()
                .name(apiField.name())
                .description(String.join("\n", apiField.description()))
                .type(apiField.nullable() ? type : GraphQLNonNull.nonNull(type))
                .build();
    }
}
