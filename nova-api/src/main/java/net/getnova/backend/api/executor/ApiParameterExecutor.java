package net.getnova.backend.api.executor;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.getnova.backend.api.data.ApiParameterData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.exception.ApiInternalParameterException;
import net.getnova.backend.api.exception.ApiParameterException;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ApiParameterExecutor {

  private static final Object[] EMPTY_PARAMETERS = new Object[0];

  private ApiParameterExecutor() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  static Object[] parseParameters(@NotNull final ApiRequest request, @NotNull final ApiParameterData[] parameters) throws ApiParameterException {
    if (request.getData() == null) return EMPTY_PARAMETERS;

    final Object[] values = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      values[i] = getParameter(request, parameters[i]);
    }

    return values;
  }

  @Nullable
  private static Object getParameter(@NotNull final ApiRequest request, @NotNull final ApiParameterData parameter) throws ApiParameterException {
    return switch (parameter.getType()) {
      case NORMAL -> {
        final JsonElement jsonValue = request.getData().get(parameter.getId()); // NullPointerException: See line 24
        if (parameter.isRequired() && (jsonValue == null || jsonValue instanceof JsonNull)) {
          throw new ApiParameterException("The parameter \"" + parameter.getId() + "\" was not found.");
        }

        try {
          yield JsonUtils.fromJson(jsonValue, parameter.getClassType());
        } catch (JsonTypeMappingException e) {
          throw new ApiInternalParameterException("Unable to parse parameter \"" + parameter.getId()
            + "\" in endpoint \"" + request.getEndpoint() + "\": " + e.getMessage(), e);
        }
      }
      case ENDPOINT -> request.getEndpoint();
      case TAG -> request.getTag();
      case DATA -> request.getData();
    };
  }
}
