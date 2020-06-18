package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
@Setter(AccessLevel.NONE)
public class QueryData {

    private final String name;
    private final String description;
    private final String deprecationReason;
    private final boolean nullable;
    private final Class<?> type;

    private final Class<?> clazz;
    private final Object instance;

    private final Set<ArgumentData> arguments;
}
