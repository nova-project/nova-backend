package net.getnova.backend.logging;

import net.getnova.backend.tests.RefectionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class NovaLogConfigurerTest {

  @Test
  void constructor() throws IllegalAccessException, InstantiationException {
    Assertions.assertTrue(RefectionUtils.checkPrivateUnsupportedOperationConstructor(NovaLogConfigurer.class));
  }

  @Test
  void redirectSysLog() {
    final PrintStream out = System.out;
    final PrintStream err = System.err;

    NovaLogConfigurer.redirectSysLog();

    assertNotEquals(out, System.out);
    assertNotEquals(err, System.err);

    System.setOut(out);
    System.setErr(err);
  }

  @Test
  void setLoglevel() {
    assertNotEquals(Level.OFF, this.getLogLevel());
    NovaLogConfigurer.setLoglevel(NovaLogLevel.OFF);
    assertEquals(Level.OFF, this.getLogLevel());
  }

  private Level getLogLevel() {
    return ((LoggerContext) LogManager.getContext(false))
      .getConfiguration()
      .getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
      .getLevel();
  }
}
