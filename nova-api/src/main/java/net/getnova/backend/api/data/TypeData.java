package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
@Setter(AccessLevel.NONE)
public class TypeData {

    private final String name;
    private final String description;
    private final Set<FieldData> fields;

    private final Class<?> clazz;
}
