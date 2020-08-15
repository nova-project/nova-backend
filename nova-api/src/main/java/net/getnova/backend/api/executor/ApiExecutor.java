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
    return endpoint == null
      ? new ApiResponse(ApiResponseStatus.NOT_FOUND, "ENDPOINT")
      : ApiEndpointExecutor.execute(new ApiContext(request), endpoint);
  }
}
