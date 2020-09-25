package net.getnova.backend.api.data;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public final class ApiRequest {

  @Nullable
  private final String endpoint;
  @Nullable
  private final JsonObject data;
  @Nullable
  private String tag;
}
