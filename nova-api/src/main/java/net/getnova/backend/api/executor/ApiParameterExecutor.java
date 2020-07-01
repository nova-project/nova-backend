package net.getnova.backend.api.executor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.getnova.backend.api.data.ApiContext;
import net.getnova.backend.api.data.ApiParameterData;
import net.getnova.backend.api.exception.ApiParameterException;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

final class ApiParameterExecutor {

    private ApiParameterExecutor() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    static Object[] parseParameters(@NotNull final ApiContext context, @NotNull final Collection<ApiParameterData> parameters) throws ApiParameterException {
        final Object[] values = new Object[parameters.size()];
        final JsonObject json = context.getRequest().getData();

        String name;
        int i = 0;
        for (final ApiParameterData parameter : parameters) {
            name = parameter.getName();
            if (json.has(name)) {
                values[i] = getParameter(context, parameter, json.get(name));
                i++;
            } else {
                throw new ApiParameterException("Parameter \"" + name + "\" wasn't found.");
            }
        }

        return values;
    }

    @NotNull
    private static Object getParameter(@NotNull final ApiContext context, @NotNull final ApiParameterData parameter, @NotNull final JsonElement json) throws ApiParameterException {
        return switch (parameter.getType()) {
            case NORMAL -> {
                try {
                    yield JsonUtils.fromJson(json, parameter.getClassType());
                } catch (JsonTypeMappingException e) {
                    throw new ApiParameterException("Unable to parse parameter \"" + parameter.getName()
                            + "\" in endpoint \"" + context.getRequest().getEndpoint() + "\":" + e.getMessage(), e);
                }
            }
            case ENDPOINT -> context.getRequest().getEndpoint();
            case TAG -> context.getRequest().getTag();
            case DATA -> context.getRequest().getData();
        };
    }
}
