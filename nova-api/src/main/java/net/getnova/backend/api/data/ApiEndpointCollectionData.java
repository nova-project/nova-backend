package net.getnova.backend.api.data;

import com.google.gson.JsonElement;
import lombok.Data;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeSet;

@Data
public final class ApiEndpointCollectionData implements JsonSerializable, Comparable<ApiEndpointCollectionData> {

  @NotNull
  private final String id;
  @NotNull
  private final String description;
  @NotNull
  private final ApiType type;
  private final boolean enabled;
  @NotNull
  private final Map<String, ApiEndpointData> endpoints;

  @NotNull
  @Override
  public JsonElement serialize() {
    return JsonBuilder
      .create("id", this.id)
      .add("description", this.description)
      .add("endpoints", new TreeSet<>(this.endpoints.values()))
      .build();
  }

  @Override
  public int compareTo(@NotNull final ApiEndpointCollectionData collectionData) {
    return this.id.compareTo(collectionData.id);
  }
}
