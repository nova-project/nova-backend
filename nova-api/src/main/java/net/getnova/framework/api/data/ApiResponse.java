package net.getnova.framework.api.data;

import com.google.gson.JsonElement;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import lombok.Setter;
import net.getnova.framework.json.JsonUtils;

@Data
public final class ApiResponse {

  private final HttpResponseStatus status;
  private final String message;
  private final JsonElement json;

  @Setter
  private String tag;

  public ApiResponse(final HttpResponseStatus status) {
    this.status = status;
    this.message = null;
    this.json = null;
  }

  public ApiResponse(final HttpResponseStatus status, final String message) {
    this.status = status;
    this.message = message;
    this.json = null;
  }

  public ApiResponse(final HttpResponseStatus status, final Object json) {
    this.status = status;
    this.message = null;
    this.json = JsonUtils.toJson(json);
  }
}
