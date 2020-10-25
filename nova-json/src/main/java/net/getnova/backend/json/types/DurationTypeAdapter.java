package net.getnova.backend.json.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Duration;

public final class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

  private static final String DURATION_CLASS_NAME = Duration.class.getName();

  @Override
  public JsonElement serialize(final Duration src, final Type typeOfSrc, final JsonSerializationContext context) {
    return new JsonPrimitive(src.toSeconds());
  }

  @Override
  public Duration deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonPrimitive()) {
      throw new JsonParseException(String.format("Unable to parse \"%s\" into a \"%s\".", json, DURATION_CLASS_NAME));
    }
    return Duration.ofSeconds(json.getAsLong());
  }
}
