package net.getnova.framework.api.executor;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiParameterData;
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.exception.ApiParameterException;
import net.getnova.framework.json.JsonUtils;

@Slf4j
final class ApiParameterExecutor {

  private static final Object[] EMPTY_PARAMETERS = new Object[0];

  private ApiParameterExecutor() {
    throw new UnsupportedOperationException();
  }

  static Object[] parseParameters(final ApiRequest request, final ApiParameterData[] parameters) throws ApiParameterException {
    if (parameters.length == 0) return EMPTY_PARAMETERS;

    final Object[] values = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      values[i] = getParameter(request, parameters[i]);
    }

    return values;
  }

  private static Object getParameter(final ApiRequest request, final ApiParameterData parameter) throws ApiParameterException {
    switch (parameter.getType()) {
      case NORMAL:
        return parseNormalParameter(request, parameter);
      case REQUEST:
        return request;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Object parseNormalParameter(final ApiRequest request, final ApiParameterData parameter) throws ApiParameterException {
    final JsonElement jsonValue = request.getData().get(parameter.getId());
    if (parameter.isRequired() && (jsonValue == null || jsonValue instanceof JsonNull)) {
      throw new ApiParameterException(String.format("PARAMETER_%s_MISSING", parameter.getId()));
    }

    try {
      return Optional.ofNullable(JsonUtils.fromJson(jsonValue, parameter.getClassType()))
        .orElseThrow(IOException::new); // Enum witch not exist returns null (Developer has "FE" and "BE"; FULL_STACK returns null)
    } catch (Throwable cause) {
//      TODO
//      if (BOOTSTRAP.isDebug() && log.isErrorEnabled()) {
//        log.error("Unable to parse parameter \"{}\" in endpoint \"{}\": {}",
//          parameter.getId(), request.getEndpoint(), cause.getMessage(), cause);
//      }
      throw new ApiParameterException(String.format("INVALID_PARAMETER_%s", parameter.getId()));
    }
  }
}
