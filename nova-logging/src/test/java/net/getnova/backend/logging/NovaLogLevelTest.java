package net.getnova.backend.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NovaLogLevelTest {

  @Test
  void getByName() {
    assertEquals(NovaLogLevel.OFF, NovaLogLevel.getByName("off"));
    assertEquals(NovaLogLevel.OFF, NovaLogLevel.getByName("OFF"));
    assertEquals(NovaLogLevel.OFF, NovaLogLevel.getByName("OfF"));
    assertNotEquals(NovaLogLevel.OFF, NovaLogLevel.getByName(" OfF"));
    assertNotEquals(NovaLogLevel.OFF, NovaLogLevel.getByName("off "));
    assertNotEquals(NovaLogLevel.OFF, NovaLogLevel.getByName("info"));
  }
}
