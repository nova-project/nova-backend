package net.getnova.framework.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.framework.api.ApiAuthenticator;
import net.getnova.framework.json.JsonBuilder;
import net.getnova.framework.json.JsonSerializable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public final class ApiEndpointData implements JsonSerializable, Comparable<ApiEndpointData> {

  private final String id;
  private final String description;
  private final ApiParameterData[] parameters;
  private final int authentication;
  private final boolean disabled;

  private final ApiAuthenticator authenticator;

  private final Object instance;
  private final Class<?> clazz;
  private final Method method;

  @Override
  public JsonElement serialize() {
    return JsonBuilder.create("id", this.id)
      .add("description", this.description)
      .add("parameters", this.getNotmalParameters())
      .add("disabled", this.disabled)
      .build();
  }

  private List<ApiParameterData> getNotmalParameters() {
    return Arrays.stream(this.parameters)
      .filter(parameter -> parameter.getType().equals(ApiParameterType.NORMAL))
      .collect(Collectors.toList());
  }

  @Override
  public int compareTo(final ApiEndpointData other) {
    return this.id.compareTo(other.id);
  }
}
