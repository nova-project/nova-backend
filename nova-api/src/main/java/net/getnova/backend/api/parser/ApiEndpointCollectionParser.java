package net.getnova.backend.api.parser;

import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiEndpointData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApiEndpointCollectionParser {

  private ApiEndpointCollectionParser() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public static Set<ApiEndpointCollectionData> parseCollections(@NotNull final Collection<Object> instances) {
    return instances.stream()
      .map(ApiEndpointCollectionParser::parseCollection)
      .filter(Objects::nonNull)
      .collect(Collectors.toUnmodifiableSet());
  }

  @NotNull
  public static Map<String, ApiEndpointData> getEndpoints(@NotNull final Set<ApiEndpointCollectionData> collections) {
    return collections.parallelStream()
      .flatMap(collection -> collection.getEndpoints().entrySet().parallelStream()
        .map(entry -> new AbstractMap.SimpleEntry<>(collection.getId() + "/" + entry.getKey(), entry.getValue())))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Nullable
  private static ApiEndpointCollectionData parseCollection(@NotNull final Object instance) {
    final Class<?> clazz = instance.getClass();
    if (!clazz.isAnnotationPresent(ApiEndpointCollection.class)) return null;

    final ApiEndpointCollection endpointCollectionAnnotation = clazz.getAnnotation(ApiEndpointCollection.class);
    return new ApiEndpointCollectionData(
      endpointCollectionAnnotation.id(),
      String.join("\n", endpointCollectionAnnotation.description()),
      endpointCollectionAnnotation.type(),
      endpointCollectionAnnotation.disabled(),
      ApiEndpointParser.parseEndpoints(instance, clazz, endpointCollectionAnnotation.disabled())
    );
  }
}
