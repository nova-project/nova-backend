package net.getnova.backend.api.executor;

import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseCode;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ApiExecutor {

    private ApiExecutor() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static ApiResponse execute(@NotNull final Map<String, ApiEndpointData> endpoints, @NotNull final ApiRequest request) {
        final ApiEndpointData endpoint = endpoints.get(request.getEndpoint());
        if (endpoint == null) return new ApiResponse(request.getTag(), ApiResponseCode.NOT_FOUND, "ENDPOINT");
        else return ApiEndpointExecutor.execute(endpoint, request);
    }
}
