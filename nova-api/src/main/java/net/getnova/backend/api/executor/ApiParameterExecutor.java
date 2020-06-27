package net.getnova.backend.api.executor;

import com.google.gson.JsonElement;
import net.getnova.backend.api.exception.ApiParameterException;

final class ApiParameterExecutor {

    private ApiParameterExecutor() {
        throw new UnsupportedOperationException();
    }

    static Object[] parseParameters(final JsonElement parameters) throws ApiParameterException {
        return null;
    }
}
