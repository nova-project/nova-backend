package net.getnova.backend.boot;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.logging.LogLevel;
import net.getnova.backend.boot.logging.LoggingConfigurator;
import net.getnova.backend.boot.logging.logback.LogbackConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ComponentScan
public class Bootstrap {

  private static final LoggingConfigurator LOGGING_CONFIGURATOR = new LogbackConfigurator(LogLevel.INFO, null);
  private final AnnotationConfigApplicationContext applicationContext;
  private final BootConfig config;

  public Bootstrap(final AnnotationConfigApplicationContext applicationContext, final BootConfig config) {
    this.applicationContext = applicationContext;
    this.config = config;
  }

  public static void main(final String[] args) {
    new AnnotationConfigApplicationContext(Bootstrap.class);
  }

  @PostConstruct
  private void postConstruct() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "shutdown"));
    this.loadConfig();
  }

  private void loadConfig() {
    if (this.config.isDebug()) {
      LOGGING_CONFIGURATOR.setLogLevel(LogLevel.INFO);
      log.info("Running in debug mode...");
    }

    try {
      LOGGING_CONFIGURATOR.setLogLevel(LogLevel.valueOf(this.config.getLevel().toUpperCase()));
    } catch (IllegalArgumentException e) {
      log.error("The log level \"{}\" could not be found. ", this.config.getLevel().toUpperCase());
    }
  }

  private void shutdown() {
    log.info("Shutting down...");
    this.applicationContext.close();
    LOGGING_CONFIGURATOR.stop();
  }

  public boolean isDebug() {
    return this.config.isDebug();
  }

  public void addConfiguration(final Class<?> clazz) {
    this.applicationContext.register(clazz);
  }

  public void refresh() {
    this.applicationContext.refresh();
  }
}
