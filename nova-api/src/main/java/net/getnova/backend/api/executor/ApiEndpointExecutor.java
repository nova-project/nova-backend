package net.getnova.backend.api.executor;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiContext;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.api.exception.ApiInternalParameterException;
import net.getnova.backend.api.exception.ApiParameterException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

@Slf4j
final class ApiEndpointExecutor {

    private ApiEndpointExecutor() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    static ApiResponse execute(@NotNull final ApiContext context, @NotNull final ApiEndpointData endpoint) {
        if (!endpoint.isEnabled()) return new ApiResponse(ApiResponseStatus.SERVICE_UNAVAILABLE, "ENDPOINT_DISABLED");

        Object[] parameters;

        try {
            parameters = ApiParameterExecutor.parseParameters(context, endpoint.getParameters());
        } catch (ApiInternalParameterException e) {
            log.error("Unable to parse parameters.", e);
            return new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (ApiParameterException e) {
            return new ApiResponse(ApiResponseStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            return (ApiResponse) endpoint.getMethod().invoke(endpoint.getInstance(), parameters);
        } catch (InvocationTargetException e) {
            log.error("A exception was thrown in endpoint \"" + endpoint.getId() + "\".", e.getTargetException());
            return new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            log.error("Unable to execute endpoint \"" + endpoint.getId() + "\".", e);
            return new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
