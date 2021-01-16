package net.getnova.framework.api.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;
import lombok.Data;
import net.getnova.framework.json.JsonWriter;

@Data
public final class ApiEndpointCollectionData implements JsonSerializable, Comparable<ApiEndpointCollectionData> {

  private final String id;
  private final String description;
  private final ApiType type;
  private final boolean disabled;
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public int compareTo(final ApiEndpointCollectionData other) {
    return this.id.compareTo(other.id);
  }

  @Override
  public void serialize(final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    JsonWriter.create(gen)
      .write("id", this.id)
      .write("description", this.description)
      .write("disabled", this.id)
      .write("endpoints", new TreeSet<>(this.endpoints.values()))
      .close();
  }

  @Override
  public void serializeWithType(JsonGenerator gen, SerializerProvider serializers,
    TypeSerializer typeSer) throws IOException {

  }
}
