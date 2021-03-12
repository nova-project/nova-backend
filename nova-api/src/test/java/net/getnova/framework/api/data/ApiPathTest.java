package net.getnova.framework.api.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ApiPathTest {

  @ParameterizedTest
  @CsvSource({
    "/hello/world/{id},\\Q/hello/world\\E\\/([^\\/]+)",
    "hello/world/{id},\\Q/hello/world\\E\\/([^\\/]+)",
    "hello/world/{id}/,\\Q/hello/world\\E\\/([^\\/]+)",
    "/hello/world/{id}/,\\Q/hello/world\\E\\/([^\\/]+)",
    "/hello/{name}/{id}/,\\Q/hello\\E\\/([^\\/]+)\\/([^\\/]+)",
    "/hello/world/{id:[A-Z]+},\\Q/hello/world\\E\\/([A-Z]+)"
  })
  void of(final String input, final String expected) {
    assertEquals(expected, ApiPath.of(input).getPattern().toString());
  }

  @Test
  void ofException() {
    assertThrows(IllegalArgumentException.class, () -> ApiPath.of("/hello/world/{id:}"));
    assertThrows(IllegalArgumentException.class, () -> ApiPath.of("/hello/world/{:[A-Z]+}"));
    assertThrows(IllegalArgumentException.class, () -> ApiPath.of("/hello/world/{:}"));
    assertThrows(IllegalArgumentException.class, () -> ApiPath.of("/hello/world/{}"));
  }

//  @Test
//  void match() {
//  }

//  @Test
//  void testToString() {
//  }
}
