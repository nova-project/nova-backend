package net.getnova.backend.boot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.context.ContextHandler;
import net.getnova.backend.boot.logging.LogLevel;
import net.getnova.backend.boot.logging.LoggingHandler;
import net.getnova.backend.boot.logging.logback.LogbackHandler;
import net.getnova.backend.boot.module.ModuleHandler;

import java.io.File;
import java.util.List;

@Slf4j
@Getter
public class Bootstrap {

  private static final List<String> BANNER = List.of(
    " _   _                     ____             _                  _",
    "| \\ | |                   |  _ \\           | |                | |",
    "|  \\| | _____   ____ _    | |_) | __ _  ___| | _____ _ __   __| |",
    "| . ` |/ _ \\ \\ / / _` |   |  _ < / _` |/ __| |/ / _ \\ '_ \\ / _` |",
    "| |\\  | (_) \\ V / (_| |   | |_) | (_| | (__|   <  __/ | | | (_| |",
    "|_| \\_|\\___/ \\_/ \\__,_|   |____/ \\__,_|\\___|_|\\_\\___|_| |_|\\__,_|"
  );

  private final long startUpTime;
  private final File workingDir;
  private final LoggingHandler loggingHandler;
  private final ModuleHandler moduleHandler;
  private final ContextHandler contextHandler;

  private boolean debug;

  public Bootstrap(final long startUpTime, final String[] args) {
    this.startUpTime = startUpTime;
    this.workingDir = new File(System.getProperty("user.dir")).getAbsoluteFile();

    this.loggingHandler = new LogbackHandler(LogLevel.INFO, BANNER, null);
    log.info("Starting in working directory \"{}\"...", this.workingDir.getPath());
    this.loadConfig();

    this.moduleHandler = new ModuleHandler(this, new File(this.workingDir, "modules"));
    this.contextHandler = new ContextHandler(Bootstrap.class, this);

    this.moduleHandler.loadModules();
    this.moduleHandler.loadDebugModules(args);

    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "shutdown"));
    if (!this.contextHandler.refresh()) this.shutdown();
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

    final String levelString = System.getenv("LOG_LEVEL");
    try {
      final LogLevel level = levelString == null ? LogLevel.WARN : LogLevel.valueOf(levelString.toUpperCase());
      if (!this.debug || !(level == LogLevel.OFF || level == LogLevel.ERROR || level == LogLevel.WARN))
        this.loggingHandler.setLogLevel(level);
    } catch (IllegalArgumentException e) {
      log.error("The log level \"{}\" could not be found.", levelString);
    }
  }

  public void shutdown() {
    final double before = System.currentTimeMillis();
    this.contextHandler.close();
    final double seconds = (System.currentTimeMillis() - before) / 1000D;

    log.info("Shutting down took {} seconds...", Math.round(seconds * 1000) / 1000D);
    this.loggingHandler.close();
  }
}
