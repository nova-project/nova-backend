package net.getnova.backend.api.data;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Set;

@Data
public class ApiEndpointData {

    private final String id;
    private final String description;
    private final Set<ApiParameterData> parameters;
    private final boolean enabled;

    private final Object instance;
    private final Class<?> clazz;
    private final Method method;
}
