package net.getnova.framework.api.data;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Optional;
import lombok.Setter;
import net.getnova.framework.json.JsonUtils;

public final class ApiResponse {

  private final HttpResponseStatus status;

  @Setter
  private String requestId;
  private String message;
  private JsonNode json;

  public ApiResponse(final HttpResponseStatus status) {
    this.status = status;
  }

  public ApiResponse(final HttpResponseStatus status, final String message) {
    if (message == null) {
      throw new NullPointerException("message is null");
    }

    this.status = status;
    this.message = message;
  }

  public ApiResponse(final HttpResponseStatus status, final Object json) {
    if (json == null) {
      throw new NullPointerException("json is null");
    }

    this.status = status;
    this.json = JsonUtils.toJson(json);
  }

  public HttpResponseStatus getStatus() {
    return this.status;
  }

  public Optional<String> getRequestId() {
    return Optional.ofNullable(this.requestId);
  }

  public Optional<String> getMessage() {
    return Optional.ofNullable(this.message);
  }

  public Optional<JsonNode> getJson() {
    return Optional.ofNullable(this.json);
  }
}
