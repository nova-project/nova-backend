package net.getnova.backend.json.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;

public final class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

  @Override
  public JsonElement serialize(final Duration src, final Type typeOfSrc, final JsonSerializationContext context) {
    return JsonUtils.toJson(src.toSeconds());
  }

  @Override
  public Duration deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    try {
      if (json.isJsonPrimitive()) {
        return Duration.ofSeconds(JsonUtils.fromJson(json, long.class));
      } else {
        throw new JsonParseException("Unable to parse a non \"" + JsonPrimitive.class.getName() + "\" into a \"" + Instant.class.getName() + "\".");
      }
    } catch (JsonTypeMappingException e) {
      throw new JsonParseException(e);
    }
  }
}
