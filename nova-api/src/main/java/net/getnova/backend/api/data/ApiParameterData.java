package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;

@Data
public final class ApiParameterData implements JsonSerializable {

  private final String id;
  private final boolean required;
  private final ApiParameterType type;
  private final String description;
  private final Class<?> classType;

  @Override
  public JsonElement serialize() {
    return JsonBuilder.create("id", this.id)
      .add("required", this.required)
      .add("description", this.description)
      .build();
  }
}
