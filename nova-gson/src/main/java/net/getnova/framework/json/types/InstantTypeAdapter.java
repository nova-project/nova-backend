package net.getnova.framework.json.types;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;

public final class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

  private static final String INSTANT_CLASS_NAME = Integer.class.getName();

  @Override
  public JsonElement serialize(final Instant src, final Type typeOfSrc, final JsonSerializationContext context) {
    return new JsonPrimitive(src.getEpochSecond());
  }

  @Override
  public Instant deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonPrimitive()) {
      throw new JsonParseException(String.format("Unable to parse \"%s\" into a \"%s\".", json, INSTANT_CLASS_NAME));
    }
    return Instant.ofEpochSecond(json.getAsLong());
  }
}
