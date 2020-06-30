package net.getnova.backend.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.PrintStream;

/**
 * This is a tool to setup the log system for the nova backend.
 *
 * @see NovaLogLevel
 */
@Slf4j
public final class NovaLogConfigurer {

    private NovaLogConfigurer() {
        throw new UnsupportedOperationException();
    }

    /**
     * Configure redirects for {@link System#out} and {@link System#err} to the logger.
     */
    public static void redirectSysLog() {
        System.setOut(new PrintStream(new NovaLogOutputStream("SysOut", NovaLogLevel.INFO)));
        System.setErr(new PrintStream(new NovaLogOutputStream("SysErr", NovaLogLevel.ERROR)));
    }

    /**
     * Sets the global log level for the application.
     *
     * @param level the log level witch should be configured
     * @see NovaLogLevel
     */
    public static void setLoglevel(final NovaLogLevel level) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.getConfiguration()
                .getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
                .setLevel(level.getLevel());
        ctx.updateLoggers();
        log.info("Using Loglevel {}.", level.name());
    }
}
