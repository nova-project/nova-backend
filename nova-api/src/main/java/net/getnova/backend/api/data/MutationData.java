package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Set;

@Data
@Setter(AccessLevel.NONE)
public class MutationData<T> {

    private final String name;
    private final String description;
    private final String deprecationReason;
    private final boolean nullable;
    private final Class<T> type;

    private final Class<?> clazz;
    private final Method method;
    private final Object instance;

    private final Set<ArgumentData> arguments;
}
