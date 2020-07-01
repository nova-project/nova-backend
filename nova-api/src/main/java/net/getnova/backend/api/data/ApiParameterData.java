package net.getnova.backend.api.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ApiParameterData {

    private final String name;
    private final boolean required;
    private final ApiParameterType type;
    private final String description;
    private final Class<?> classType;
}
