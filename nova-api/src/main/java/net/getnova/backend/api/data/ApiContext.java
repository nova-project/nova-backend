package net.getnova.backend.api.data;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class ApiContext {

  @NotNull
  private final ApiRequest request;
}
