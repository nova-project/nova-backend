package net.getnova.backend.api.data;

import lombok.Data;

import java.util.Map;

@Data
public class ApiEndpointCollectionData {

    private final String id;
    private final String description;
    private final Map<String, ApiEndpointData> endpoints;
}
