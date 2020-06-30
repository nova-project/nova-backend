package net.getnova.backend.api.executor;

import com.google.gson.JsonElement;
import net.getnova.backend.api.exception.ApiParameterException;
import org.jetbrains.annotations.NotNull;

final class ApiParameterExecutor {

    private ApiParameterExecutor() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    static Object[] parseParameters(@NotNull final JsonElement parameters) throws ApiParameterException {
        return null;
    }
}
