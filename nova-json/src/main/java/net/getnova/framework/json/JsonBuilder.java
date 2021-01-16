package net.getnova.framework.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@link JsonBuilder} is a tool to create a {@link ObjectNode}.
 * <br>
 * Example:
 *
 * <pre>{@code
 * final JsonObject json = JsonBuilder.create("name", "Max")
 *    .add("age", 43)
 *    .build();
 * }</pre>
 * or
 * <pre>{@code
 * final String json = JsonBuilder.create("name", "Max")
 *    .add("age", 43)
 *    .toString();
 * }</pre>
 *
 * @see JsonUtils
 * @see JsonWriter
 */
@EqualsAndHashCode
@Slf4j
public final class JsonBuilder {

  private final ObjectNode node;

  private JsonBuilder(final ObjectNode node) {
    this.node = node;
  }

  /**
   * Creates a {@link JsonBuilder} without any data.
   *
   * @return the JsonBuilder
   * @see JsonBuilder#create(String, Object)
   */
  private static JsonBuilder create() {
    return create(JsonUtils.objectMapper.createObjectNode());
  }

  /**
   * Creates a {@link JsonBuilder} without any data.
   *
   * @param json the existing {@link JsonNode}
   * @return the JsonBuilder
   * @see JsonBuilder#create(Object)
   * @see JsonBuilder#create(String, Object)
   */
  public static JsonBuilder create(final ObjectNode json) {
    return new JsonBuilder(json.deepCopy());
  }

  /**
   * Creates a {@link JsonBuilder} without any data.
   *
   * @param object the data
   * @return the JsonBuilder
   * @see JsonBuilder#create(ObjectNode)
   * @see JsonBuilder#create(String, Object)
   */
  public static JsonBuilder create(final Object object) {
    return new JsonBuilder(JsonUtils.toJson(object).require());
  }

  /**
   * Creates a {@link JsonBuilder} with one Key, Value pair.
   *
   * @param key   the key of the pair
   * @param value the value of the pair
   * @return the JsonBuilder
   * @see JsonBuilder#create(ObjectNode)
   * @see JsonBuilder#create(Object)
   */
  public static JsonBuilder create(final String key, final Object value) {
    return JsonBuilder.create().add(key, value);
  }

  /**
   * This adds a Key, Value pair to the Json, and return this instance to call other functions to manipulate the Json.
   *
   * @param key   the key of the pair
   * @param value the value of the pair
   * @return a instance if this to call other functions without saving {@link JsonBuilder} to a variable
   */
  public JsonBuilder add(final String key, final Object value) {
    this.node.set(key, JsonUtils.toJson(value));
    return this;
  }

  /**
   * Removes the {@code property} from this {@link JsonNode}.
   *
   * @param key name of the member that should be removed.
   * @return the {@link JsonNode} object that is being removed.
   */
  public Optional<JsonNode> remove(final String key) {
    return Optional.ofNullable(this.node.remove(key));
  }

  /**
   * Converts this to an {@link JsonNode}.
   *
   * @return an {@link JsonNode} with the values from this
   * @see JsonBuilder#toString()
   */
  public ObjectNode build() {
    return this.node;
  }

  /**
   * Converts this to a Json-{@link String}.
   *
   * @return a Json-{@link String} with the values from this
   * @see JsonBuilder#build()
   */
  @Override
  public String toString() {
    return this.build().toString();
  }
}
