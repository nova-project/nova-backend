package net.getnova.framework.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

public final class JsonUtils {

  public static final ObjectMapper objectMapper = new ObjectMapper()
    .registerModules(
      new Jdk8Module(),
      new JavaTimeModule()
    )
    .setLocale(Locale.ENGLISH);
  // objectMapper.registerModule(new Hibernate5Module());

  private JsonUtils() {
    throw new UnsupportedOperationException();
  }

  public static JsonNode read(final String content) throws JsonProcessingException {
    return objectMapper.readTree(content);
  }

  public static JsonNode read(final InputStream is, final Charset charset) throws IOException {
    try (Reader reader = new InputStreamReader(is, charset)) {
      return objectMapper.readTree(reader);
    }
  }

  public static String toString(final Object data) throws JsonProcessingException {
    return objectMapper.writeValueAsString(data);
  }

  public static void write(final Object data, final OutputStream os, final Charset charset) throws IOException {
    try (Writer writer = new OutputStreamWriter(os, charset)) {
      objectMapper.writeValue(writer, data);
    }
  }

  public static <T> T fromJson(final TreeNode node, final Class<T> clazz) throws JsonProcessingException {
    return objectMapper.treeToValue(node, clazz);
  }

  public static JsonNode toJson(final Object object) {
    return objectMapper.valueToTree(object);
  }
}
