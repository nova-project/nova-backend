package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.Resource;

@Getter
@EqualsAndHashCode
public final class ApiResponse {

  @NotNull
  private final ApiResponseStatus responseCode;

  @Nullable
  private final String message;

  @Nullable
  private final Data data;

  @Setter
  @Nullable
  private String tag;

  public ApiResponse(@NotNull final ApiResponseStatus responseCode) {
    this.responseCode = responseCode;
    this.message = null;
    this.data = null;
  }

  public ApiResponse(@NotNull final ApiResponseStatus responseCode, @NotNull final String message) {
    this.responseCode = responseCode;
    this.message = message;
    this.data = null;
  }

  public ApiResponse(@NotNull final ApiResponseStatus responseCode, @NotNull final Resource resource) {
    this.responseCode = responseCode;
    this.message = null;
    this.data = new Data(resource);
  }

  public ApiResponse(@NotNull final ApiResponseStatus responseCode, @NotNull final Object json) {
    this.responseCode = responseCode;
    this.message = null;
    this.data = new Data(JsonUtils.toJson(json));
  }

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class Data {

    @Nullable
    private final JsonElement json;
    @Nullable
    private final Resource resource;

    public Data(@NotNull final JsonElement json) {
      this.json = json;
      this.resource = null;
    }

    public Data(@NotNull final Resource resource) {
      this.json = null;
      this.resource = resource;
    }

    public boolean isJson() {
      return this.json != null;
    }

    public boolean isResource() {
      return this.resource != null;
    }
  }
}
