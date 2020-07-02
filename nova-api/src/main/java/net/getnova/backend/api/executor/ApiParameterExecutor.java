package net.getnova.backend.api.executor;

import com.google.gson.JsonObject;
import net.getnova.backend.api.data.ApiContext;
import net.getnova.backend.api.data.ApiParameterData;
import net.getnova.backend.api.exception.ApiInternalParameterException;
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

        int i = 0;
        for (final ApiParameterData parameter : parameters) {
            values[i] = getParameter(context, parameter);
            i++;
        }

        return values;
    }

    @NotNull
    private static Object getParameter(@NotNull final ApiContext context, @NotNull final ApiParameterData parameter) throws ApiParameterException {
        switch (parameter.getType()) {
            case NORMAL:
                final JsonObject data = context.getRequest().getData();
                final String id = parameter.getId();
                if (!data.has(id)) {
                    throw new ApiParameterException("The parameter \"" + id + "\" was not found.");
                }
                try {
                    return JsonUtils.fromJson(data.get(id), parameter.getClassType());
                } catch (JsonTypeMappingException e) {
                    throw new ApiInternalParameterException("Unable to parse parameter \"" + id
                            + "\" in endpoint \"" + context.getRequest().getEndpoint() + "\": " + e.getMessage(), e);
                }
            case ENDPOINT:
                return context.getRequest().getEndpoint();
            case TAG:
                return context.getRequest().getTag();
            case DATA:
                return context.getRequest().getData();
            default:
                throw new IllegalArgumentException();
        }
    }
}
