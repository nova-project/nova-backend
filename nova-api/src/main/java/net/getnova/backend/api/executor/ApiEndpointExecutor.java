package net.getnova.backend.api.executor;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseCode;
import net.getnova.backend.api.exception.ApiParameterException;

import java.lang.reflect.InvocationTargetException;

@Slf4j
final class ApiEndpointExecutor {

    private ApiEndpointExecutor() {
        throw new UnsupportedOperationException();
    }

    static ApiResponse execute(final ApiEndpointData endpoint, final ApiRequest request) {
        Object[] parameters;

        try {
            parameters = ApiParameterExecutor.parseParameters(request.getData());
        } catch (ApiParameterException e) {
            return new ApiResponse(ApiResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        try {
            return (ApiResponse) endpoint.getMethod().invoke(endpoint.getInstance(), parameters);
        } catch (InvocationTargetException e) {
            log.error("A exception was thrown in endpoint \"" + endpoint.getName() + "\".", e.getTargetException());
            return new ApiResponse(ApiResponseCode.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            log.error("Unable to execute endpoint \"" + endpoint.getName() + "\".", e);
            return new ApiResponse(ApiResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}

