package net.getnova.backend.api.data;

import lombok.Data;

import java.util.Set;

@Data
public class TypeData {

    private final String name;
    private final String description;
    private final Set<FieldData> fields;

    private final Class<?> clazz;
}
