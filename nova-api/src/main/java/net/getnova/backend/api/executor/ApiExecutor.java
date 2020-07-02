package net.getnova.backend.api.executor;

import net.getnova.backend.api.data.ApiContext;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ApiExecutor {

    private ApiExecutor() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public static ApiResponse execute(@NotNull final Map<String, ApiEndpointData> endpoints, @NotNull final ApiRequest request) {
        final ApiEndpointData endpoint = endpoints.get(request.getEndpoint());
        if (endpoint == null) return new ApiResponse(request.getTag(), ApiResponseStatus.NOT_FOUND, "ENDPOINT");
        else return ApiEndpointExecutor.execute(new ApiContext(request), endpoint);
    }
}
