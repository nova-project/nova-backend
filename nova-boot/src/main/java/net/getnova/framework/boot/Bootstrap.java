package net.getnova.framework.boot;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.boot.context.ContextHandler;
import net.getnova.framework.boot.logging.LogLevel;
import net.getnova.framework.boot.logging.LoggingHandler;
import net.getnova.framework.boot.logging.logback.LogbackHandler;
import net.getnova.framework.boot.module.ModuleHandler;

@Slf4j
@Getter
public final class Bootstrap {

  private static final List<String> BANNER = List.of(
    "  _   _                    _____                                            _    ",
    " | \\ | | _____   ____ _   |  ___| __ __ _ _ __ ___   _____      _____  _ __| | __",
    " |  \\| |/ _ \\ \\ / / _` |  | |_ | '__/ _` | '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ /",
    " | |\\  | (_) \\ V / (_| |  |  _|| | | (_| | | | | | |  __/\\ V  V / (_) | |  |   < ",
    " |_| \\_|\\___/ \\_/ \\__,_|  |_|  |_|  \\__,_|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_\\"
  );

  private final long startUpTime;
  private final String[] debugModules;
  private final File workingDir;
  private final LoggingHandler loggingHandler;
  private final ModuleHandler moduleHandler;
  private final ContextHandler contextHandler;

  private boolean debug;

  private Bootstrap(final long startUpTime, final String[] args) {
    this.startUpTime = startUpTime;
    this.workingDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
    this.debugModules = args;

    this.loggingHandler = new LogbackHandler(LogLevel.INFO, BANNER, System.getenv("SENTRY_DSN"));
    log.info("Starting in working directory \"{}\"...", this.workingDir.getPath());
    this.loadConfig();

    this.moduleHandler = new ModuleHandler(this, new File(this.workingDir, "modules"));
    this.contextHandler = new ContextHandler(Bootstrap.class, this);
    this.contextHandler.register(ModuleHandler.class, this.moduleHandler);

    this.moduleHandler.loadModules();
    this.moduleHandler.loadDebugModules(this.debugModules);
    this.contextHandler.setClassLoader(this.moduleHandler.getLoader());

    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown0, "shutdown"));
    if (!this.contextHandler.refresh()) {
      this.shutdown();
    }
  }

  public static void main(final String[] args) {
    final long before = System.currentTimeMillis();
    new Bootstrap(before, args);
    final double seconds = (System.currentTimeMillis() - before) / 1000D;
    log.info("The bootstrapping was finished within {} seconds.", Math.round(seconds * 1000) / 1000D);
  }

  private void loadConfig() {
    this.debug = Boolean.parseBoolean(System.getenv("DEBUG"));

    if (this.debug) {
      this.loggingHandler.setLogLevel(LogLevel.INFO);
      log.info("Running in debug mode...");
    }

    try {
      final LogLevel level = Optional.ofNullable(System.getenv("LOG_LEVEL"))
        .map(levelString -> LogLevel.valueOf(levelString.toUpperCase()))
        .orElse(LogLevel.WARN);

      if (!this.debug || !(level == LogLevel.OFF || level == LogLevel.ERROR || level == LogLevel.WARN)) {
        this.loggingHandler.setLogLevel(level);
      }

    }
    catch (IllegalArgumentException e) {
      log.error(
        "The selected log level could not be found. ({})",
        Arrays.stream(LogLevel.values())
          .map(Enum::toString)
          .collect(Collectors.joining(", "))
      );
      this.shutdown();
    }
  }

  public void shutdown() {
    new Thread(() -> System.exit(0), "exit").start();
  }

  private void shutdown0() {
    log.info("Shutting down...");

    final double before = System.currentTimeMillis();
    this.contextHandler.close();
    final double seconds = (System.currentTimeMillis() - before) / 1000D;

    log.info("Shutting down took {} seconds...", Math.round(seconds * 1000) / 1000D);
    this.loggingHandler.close();
  }
}
