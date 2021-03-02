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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class OffsetDateTimeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {

  @Override
  public JsonElement serialize(final OffsetDateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
    return new JsonPrimitive(src.toEpochSecond());
  }

  @Override
  public OffsetDateTime deserialize(final JsonElement json, final Type typeOfT,
    final JsonDeserializationContext context) throws JsonParseException {
    if (!json.isJsonPrimitive()) {
      throw new JsonParseException(
        "Unable to parse a non \"" + JsonPrimitive.class.getName() + "\" into a \"" + OffsetDateTime.class.getName()
          + "\".");
    }
    return Instant.ofEpochSecond(json.getAsLong()).atOffset(ZoneOffset.UTC);
  }
}
