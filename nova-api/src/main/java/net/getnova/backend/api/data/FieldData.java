package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Field;

@Data
@Setter(AccessLevel.NONE)
public class FieldData {

    private final String name;
    private final String description;
    private final String deprecationReason;
    private final boolean nullable;
    private final Class<?> type;

    private final Class<?> clazz;
    private final Field field;
}
