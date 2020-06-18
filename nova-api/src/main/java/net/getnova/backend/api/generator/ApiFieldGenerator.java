package net.getnova.backend.api.generator;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLOutputType;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiField;
import net.getnova.backend.api.data.FieldData;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
final class ApiFieldGenerator {

    private ApiFieldGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLFieldDefinition generateGraphQLField(final FieldData argumentData) {
        final GraphQLOutputType type = Scalars.GraphQLString;

        return GraphQLFieldDefinition.newFieldDefinition()
                .name(argumentData.getName())
                .description(argumentData.getDescription())
                .type(argumentData.isNullable() ? type : GraphQLNonNull.nonNull(type)) // TODO: Type
                .build();
    }

    static Set<FieldData> generateFields(final Class<?> clazz) {
        final Set<FieldData> fields = new LinkedHashSet<>();

        FieldData fieldData;
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldData = generateField(clazz, field);
            field.setAccessible(false);
            if (fieldData != null) fields.add(fieldData);
        }

        return fields;
    }

    static FieldData generateField(final Class<?> clazz, final Field field) {
        if (!field.isAnnotationPresent(ApiField.class)) {
            log.error("Api field {}.{} does not has annotation {}.", clazz.getName(), field.getName(), ApiField.class.getName());
            return null;
        }

        final ApiField fieldAnnotation = field.getAnnotation(ApiField.class);

        return new FieldData(fieldAnnotation.name(),
                String.join("\n", fieldAnnotation.description()),
                fieldAnnotation.deprecationReason(),
                fieldAnnotation.nullable(),
                field.getType(),
                clazz, field);
    }
}
