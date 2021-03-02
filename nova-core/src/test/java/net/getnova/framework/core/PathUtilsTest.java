package net.getnova.framework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathUtilsTest {

  @ParameterizedTest
  @CsvSource({
    "/hello,/hello/world,/world",
    "/hello/,/hello/world,/world",
    "/hello,/hello/world/,/world",
    "/hello/,/hello/world/,/world",
    "/hello,hello/world,/world",
    "/hello/,hello/world,/world",
    "/hello,hello/world/,/world",
    "/hello/,hello/world/,/world",
    "/,/hello/world,/hello/world",
    "/,/hello/world/,/hello/world",
    "/,hello/world,/hello/world",
    "/,hello/world/,/hello/world",
  })
  void stripSegments(final String segments, final String path, final String output) {
    assertEquals(output, PathUtils.stripSegments(segments, path));
  }

  @Test
  void stripSegments_InvalidSegment() {
    assertThrows(IllegalArgumentException.class, () -> PathUtils.stripSegments("hello", "/hello/world"));
  }

  @ParameterizedTest
  @CsvSource({
    "/hello/world/,/hello/world",
    "/hello/world,/hello/world",
    "hello/world/,/hello/world",
    "hello/world,/hello/world"
  })
  void normalizePath(final String input, final String output) {
    assertEquals(output, PathUtils.normalizePath(input));
  }

  @Test
  void normalizePath_Slash() {
    assertEquals("/", PathUtils.normalizePath("/"));
  }

  @Test
  void normalizePath_Empty() {
    assertEquals("/", PathUtils.normalizePath(""));
  }
}
