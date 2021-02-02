package net.getnova.framework.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import lombok.EqualsAndHashCode;

/**
 * The {@link JsonWriter} is a tool to provide data to a {@link JsonGenerator} without much boilerplate code.
 * <br>
 * Example:
 *
 * <pre>{@code
 * final JsonGenerator generator = ...;
 * final JsonObject json = JsonBuilder.create(generator)
 *    .add("name", "Max")
 *    .add("age", 43)
 *    .build();
 * }</pre>
 *
 * @see JsonUtils
 * @see JsonBuilder
 */
@EqualsAndHashCode
public final class JsonWriter implements AutoCloseable {

  private final JsonGenerator generator;

  private JsonWriter(final JsonGenerator generator) throws IOException {
    this.generator = generator;
    this.generator.writeStartObject();
  }

  /**
   * Creates a {@link JsonWriter} with a pre filled {@link JsonGenerator}.
   *
   * @param generator the existing {@link JsonGenerator}
   * @return the JsonBuilder
   */
  public static JsonWriter create(final JsonGenerator generator) throws IOException {
    return new JsonWriter(generator);
  }

  public JsonWriter write(final String key) throws IOException {
    this.generator.writeNullField(key);
    return this;
  }

  public JsonWriter write(final ObjectNode node) throws IOException {
    this.generator.writeObject(node);
    return this;
  }

  /**
   * This adds a Key, Value pair to the Json, and return this instance to call other functions to manipulate the Json.
   *
   * @param key   the key of the pair
   * @param value the value of the pair
   * @return a instance if this to call other functions without saving {@link JsonBuilder} to a variable
   */
  public JsonWriter write(final String key, final Object value) throws IOException {
    this.generator.writeObjectField(key, value);
    return this;
  }

  @Override
  public void close() throws IOException {
    this.generator.writeEndObject();
  }
}
