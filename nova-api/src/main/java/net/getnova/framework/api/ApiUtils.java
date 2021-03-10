package net.getnova.framework.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.exception.ResponseApiException;

public class ApiUtils {

  private ApiUtils() {
    throw new UnsupportedOperationException();
  }

  public static Throwable mapJsonError(final Throwable cause) {
    if (cause instanceof InvalidDefinitionException) {
      return cause;
    }

    if (cause instanceof JsonParseException) {
      return new ResponseApiException(ApiResponse.of(HttpResponseStatus.BAD_REQUEST,
        "JSON", "SYNTAX"), cause);
    }

    if (cause instanceof MismatchedInputException) {
      return new ResponseApiException(ApiResponse.of(HttpResponseStatus.BAD_REQUEST,
        "JSON", "UNEXPECTED_CONTENT"), cause);
    }

    return cause;
  }
}
