package net.nova_project.backend.logging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;

/**
 * Levels used for identifying the severity of an event. Levels are organized from most specific to least:
 * <ul>
 * <li>{@link NovaLogLevel#OFF} (most specific, no logging)</li>
 * <li>{@link NovaLogLevel#FATAL} (most specific, little data)</li>
 * <li>{@link NovaLogLevel#ERROR}</li>
 * <li>{@link NovaLogLevel#WARN}</li>
 * <li>{@link NovaLogLevel#INFO}</li>
 * <li>{@link NovaLogLevel#DEBUG}</li>
 * <li>{@link NovaLogLevel#TRACE} (least specific, a lot of data)</li>
 * <li>{@link NovaLogLevel#ALL} (least specific, all data)</li>
 * </ul>
 * <p>
 * Typically, configuring a level in a filter or on a logger will cause logging events of that level and those that are
 * more specific to pass through the filter. A special level, {@link #ALL}, is guaranteed to capture all levels when
 * used in logging configurations.
 */
@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public enum NovaLogLevel {

    /**
     * No events will be logged.
     */
    OFF(Level.OFF),

    /**
     * A severe error that will prevent the application from continuing.
     */
    FATAL(Level.FATAL),

    /**
     * An error in the application, possibly recoverable.
     */
    ERROR(Level.ERROR),

    /**
     * An event that might possible lead to an error.
     */
    WARN(Level.WARN),

    /**
     * An event for informational purposes.
     */
    INFO(Level.INFO),

    /**
     * A general debugging event.
     */
    DEBUG(Level.DEBUG),

    /**
     * A fine-grained debug message, typically capturing the flow through the application.
     */
    TRACE(Level.TRACE),

    /**
     * All events should be logged.
     */
    ALL(Level.ALL);

    /**
     * Holden the {@code Log4j2} version of the loglevel.
     */
    private final Level level;
}
