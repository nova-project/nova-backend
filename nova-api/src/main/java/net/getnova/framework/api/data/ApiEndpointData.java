package net.getnova.framework.api.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import net.getnova.framework.api.ApiAuthenticator;
import net.getnova.framework.json.JsonWriter;

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

  private List<ApiParameterData> getNotmalParameters() {
    return Arrays.stream(this.parameters)
      .filter(parameter -> parameter.getType().equals(ApiParameterType.NORMAL))
      .collect(Collectors.toList());
  }

  @Override
  public int compareTo(final ApiEndpointData other) {
    return this.id.compareTo(other.id);
  }

  @Override
  public void serialize(final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    JsonWriter.create(gen)
      .write("id", this.id)
      .write("description", this.description)
      .write("parameters", this.getNotmalParameters())
      .write("disabled", this.disabled)
      .close();
  }

  @Override
  public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
    throws IOException {

  }
}
