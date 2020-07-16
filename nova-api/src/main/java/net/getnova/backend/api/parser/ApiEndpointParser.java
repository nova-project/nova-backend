package net.getnova.backend.api.parser;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiEndpoint;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiParameterData;
import net.getnova.backend.api.data.ApiResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
final class ApiEndpointParser {

    private ApiEndpointParser() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    static Map<String, ApiEndpointData> parseEndpoints(@NotNull final Object object, @NotNull final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .map(method -> parseEndpoint(object, clazz, method))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(ApiEndpointData::getId, Function.identity()));
    }

    @Nullable
    private static ApiEndpointData parseEndpoint(@NotNull final Object instance, @NotNull final Class<?> clazz, @NotNull final Method method) {
        final boolean hasAccess;
        try {
            hasAccess = method.canAccess(instance);
        } catch (IllegalArgumentException e) {
            return null;
        }

        if (!hasAccess) if (!method.trySetAccessible()) {
            return null;
        }

        if (!method.isAnnotationPresent(ApiEndpoint.class)) {
            if (!hasAccess) method.setAccessible(false);
            return null;
        }

        if (!method.getReturnType().equals(ApiResponse.class)) {
            log.error("Endpoint {}.{} cannot be parsed because it does not have the return type {}.", clazz.getName(), method.getName(), ApiResponse.class.getName());
            if (!hasAccess) method.setAccessible(false);
            return null;
        }

        final ApiEndpoint endpointAnnotation = method.getAnnotation(ApiEndpoint.class);
        final Set<ApiParameterData> parameters = ApiParameterParser.parseParameters(clazz, method);

        return new ApiEndpointData(endpointAnnotation.id(),
                String.join("\n", endpointAnnotation.description()),
                parameters == null ? Collections.emptySet() : parameters,
                parameters != null,
                instance, clazz, method);
    }
}
