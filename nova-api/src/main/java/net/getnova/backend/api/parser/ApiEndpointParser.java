package net.getnova.backend.api.parser;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiEndpoint;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiParameterData;
import net.getnova.backend.api.data.ApiResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
final class ApiEndpointParser {

  private static final ApiParameterData[] EMPTY_PARAMETERS = new ApiParameterData[0];

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

    if (!hasAccess && !method.trySetAccessible()) return null;

    if (!method.isAnnotationPresent(ApiEndpoint.class)) {
      if (!hasAccess) method.setAccessible(false);
      return null;
    }

    final Class<?> returnType = method.getReturnType();
    if (!(returnType.equals(ApiResponse.class) || returnType.equals(Mono.class))) {
      log.error("Endpoint {}.{} cannot be parsed because it does not have the return type {} or {}<{}>.",
        clazz.getName(), method.getName(), ApiResponse.class.getName(), Mono.class.getName(), ApiResponse.class.getName());
      if (!hasAccess) method.setAccessible(false);
      return null;
    }

    final ApiEndpoint endpointAnnotation = method.getAnnotation(ApiEndpoint.class);
    final ApiParameterData[] parameters = ApiParameterParser.parseParameters(clazz, method);

    return new ApiEndpointData(
      endpointAnnotation.id(),
      String.join("\n", endpointAnnotation.description()),
      parameters == null || parameters.length == 0 ? EMPTY_PARAMETERS : parameters,
      parameters != null,
      instance,
      clazz,
      method
    );
  }
}
