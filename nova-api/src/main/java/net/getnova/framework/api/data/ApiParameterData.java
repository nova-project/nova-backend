package net.getnova.framework.api.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import lombok.Data;
import net.getnova.framework.json.JsonWriter;

@Data
public final class ApiParameterData implements JsonSerializable {

  private final String id;
  private final boolean required;
  private final ApiParameterType type;
  private final String description;
  private final Class<?> classType;

  @Override
  public void serialize(final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    JsonWriter.create(gen)
      .write("id", this.id)
      .write("required", this.required)
      .write("description", this.description)
      .close();
  }

  @Override
  public void serializeWithType(final JsonGenerator gen, final SerializerProvider serializers,
    final TypeSerializer typeSer) {
  }
}
