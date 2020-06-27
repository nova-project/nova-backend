package net.getnova.backend.api.executor;

import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseCode;

import java.util.Map;

public final class ApiExecutor {

    private ApiExecutor() {
        throw new UnsupportedOperationException();
    }

    public static ApiResponse execute(final Map<String, ApiEndpointData> endpoints, final ApiRequest request) {
        final ApiEndpointData endpoint = endpoints.get(request.getEndpoint());
        if (endpoint == null) return new ApiResponse(request.getTag(), ApiResponseCode.NOT_FOUND, "ENDPOINT");
        else return ApiEndpointExecutor.execute(endpoint, request);
    }
}
