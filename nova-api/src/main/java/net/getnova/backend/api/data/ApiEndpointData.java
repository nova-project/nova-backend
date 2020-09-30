package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
public final class ApiEndpointData implements JsonSerializable, Comparable<ApiEndpointData> {

  @NotNull
  private final String id;
  @NotNull
  private final String description;
  @NotNull
  private final ApiParameterData[] parameters;
  private final boolean enabled;

  @NotNull
  private final Object instance;
  @NotNull
  private final Class<?> clazz;
  @NotNull
  private final Method method;

  @NotNull
  @Override
  public JsonElement serialize() {
    return JsonBuilder
      .create("id", this.id)
      .add("description", this.description)
      .add("parameters", Arrays.stream(this.parameters)
        .filter(parameter -> parameter.getType().equals(ApiParameterType.NORMAL))
        .collect(Collectors.toList()))
      .add("enabled", this.enabled)
      .build();
  }

  @Override
  public int compareTo(@NotNull final ApiEndpointData endpointData) {
    return this.id.compareTo(endpointData.id);
  }
}
