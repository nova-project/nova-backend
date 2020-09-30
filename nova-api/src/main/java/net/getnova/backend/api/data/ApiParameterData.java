package net.getnova.backend.api.data;

import lombok.Data;
import net.getnova.backend.json.JsonTransient;
import org.jetbrains.annotations.NotNull;

@Data
public final class ApiParameterData {

  @NotNull
  private final String id;
  private final boolean required;
  @JsonTransient
  @NotNull
  private final ApiParameterType type;
  @NotNull
  private final String description;
  @JsonTransient
  @NotNull
  private final Class<?> classType;
}
