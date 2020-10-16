package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;

import java.util.Map;

@Data
public final class ApiEndpointCollectionData implements JsonSerializable {

  private final String id;
  private final String description;
  private final ApiType type;
  private final boolean enabled;
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public JsonElement serialize() {
    return JsonBuilder.create("id", this.id)
      .add("description", this.description)
      .add("endpoints", this.endpoints.values())
      .build();
  }
}
