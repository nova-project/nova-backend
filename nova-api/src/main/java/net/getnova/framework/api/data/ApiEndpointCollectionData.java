package net.getnova.framework.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.framework.json.JsonBuilder;
import net.getnova.framework.json.JsonSerializable;

import java.util.Map;
import java.util.TreeSet;

@Data
public final class ApiEndpointCollectionData implements JsonSerializable, Comparable<ApiEndpointCollectionData> {

  private final String id;
  private final String description;
  private final ApiType type;
  private final boolean disabled;
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public JsonElement serialize() {
    return JsonBuilder.create("id", this.id)
      .add("description", this.description)
      .add("disabled", this.id)
      .add("endpoints", new TreeSet<>(this.endpoints.values()))
      .build();
  }

  @Override
  public int compareTo(final ApiEndpointCollectionData other) {
    return this.id.compareTo(other.id);
  }
}
