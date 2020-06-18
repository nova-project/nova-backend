package net.getnova.backend.api.generator;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiField;
import net.getnova.backend.api.data.FieldData;
import net.getnova.backend.api.types.ApiStringTypeMapping;
import net.getnova.backend.api.types.ApiTypeMapping;
import net.getnova.backend.api.types.ApiTypeMappingHandler;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
final class ApiFieldGenerator {

    private ApiFieldGenerator() {
        throw new UnsupportedOperationException();
    }

    static GraphQLFieldDefinition generateGraphQLField(final FieldData argumentData) {

        ApiTypeMapping type = ApiTypeMappingHandler.INSTANCE.getType(argumentData.getType());
        if (type == null) {
            log.error("Unable to find graphql type mapping for {}.", argumentData.getType().getName());
            type = new ApiStringTypeMapping();
        }

        return GraphQLFieldDefinition.newFieldDefinition()
                .name(argumentData.getName())
                .description(argumentData.getDescription())
                .type(argumentData.isNullable() ? type.getGraphQlScalar() : GraphQLNonNull.nonNull(type.getGraphQlScalar()))
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
