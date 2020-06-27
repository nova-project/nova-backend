package net.getnova.backend.api.parser;

import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiEndpointData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApiEndpointCollectionParser {

    private ApiEndpointCollectionParser() {
        throw new UnsupportedOperationException();
    }

    public static Set<ApiEndpointCollectionData> parseCollections(final Set<Object> instances) {
        return instances.stream()
                .map(ApiEndpointCollectionParser::parseCollection)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Map<String, ApiEndpointData> getEndpoints(final Set<ApiEndpointCollectionData> collections) {
        final Map<String, ApiEndpointData> endpoints = new HashMap<>();

        String collectionName;
        for (ApiEndpointCollectionData collection : collections) {
            collectionName = collection.getName().toLowerCase();
            for (final Map.Entry<String, ApiEndpointData> entry : collection.getEndpoints().entrySet())
                endpoints.put(collectionName + "/" + entry.getKey().toLowerCase(), entry.getValue());
        }

        return endpoints;
    }

    private static ApiEndpointCollectionData parseCollection(final Object instance) {
        final Class<?> clazz = instance.getClass();
        if (!clazz.isAnnotationPresent(ApiEndpointCollection.class)) return null;

        final ApiEndpointCollection endpointCollectionAnnotation = clazz.getAnnotation(ApiEndpointCollection.class);
        return new ApiEndpointCollectionData(endpointCollectionAnnotation.name(),
                String.join("\n", endpointCollectionAnnotation.description()),
                ApiEndpointParser.parseEndpoints(instance, clazz));
    }
}
