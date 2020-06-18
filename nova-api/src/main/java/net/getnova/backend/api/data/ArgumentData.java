package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Data
@Setter(AccessLevel.NONE)
public class ArgumentData {

    private final String name;
    private final String description;
    private final boolean nullable;
    private final Class<?> type;

    private final Class<?> clazz;
    private final Method method;
    private final Parameter parameter;
}
