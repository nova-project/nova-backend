package net.getnova.framework.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.exception.ResponseApiException;

public class ApiUtils {

  private ApiUtils() {
    throw new UnsupportedOperationException();
  }

  public static Throwable mapJsonError(final Throwable cause) {
    if (cause instanceof JsonParseException) {
      return new ResponseApiException(ApiResponse.of(HttpResponseStatus.BAD_REQUEST, "JSON", "SYNTAX"));
    }

    if (cause instanceof JsonMappingException) {
      return new ResponseApiException(ApiResponse.of(HttpResponseStatus.BAD_REQUEST, "JSON", "UNEXPECTED_CONTENT"));
    }

    if (cause instanceof JsonProcessingException) {
      return new ResponseApiException(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
    }

    return cause;
  }
}
