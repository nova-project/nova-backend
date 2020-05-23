package net.nova_project.backend.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This {@link OutputStream} redirects all contents in which it is written to a logger.
 * Its used primary for {@link System#out} and {@link System#err}.
 *
 * @see NovaLogConfigurer#redirectSysLog()
 */
class NovaLogOutputStream extends OutputStream {

    private static final String LINE_SEPARATOR = "\n";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final Logger logger;
    private final Level level;
    private final PrintStream original;
    private String memory;

    /**
     * Creates a new {@link OutputStream} witch redirects
     * all contents in which it is written to a logger.
     *
     * @param name     the name for the new created logger
     * @param logLevel the {@link NovaLogLevel} in witch the content from
     *                 the stream should be logged.
     * @param original the origin {@link PrintStream} e.g. {@link System#out} or {@link System#err}.
     *                 Its used to print exceptions without the logger prefix. ([timestamp] [thread] ...)
     *                 Set this to {@code null} to disable this feature.
     */
    NovaLogOutputStream(final String name, final NovaLogLevel logLevel, final PrintStream original) {
        this.logger = LogManager.getLogger(name);
        this.level = logLevel.getLevel();
        this.original = original;
        this.memory = "";
    }

    @Override
    public void write(final int b) {
        final byte[] bytes = new byte[1];
        bytes[0] = (byte) (b & 0xff);
        final String current = new String(bytes, CHARSET);

        if (!current.equals(LINE_SEPARATOR)) this.memory += current;
        else flush();
    }

    @Override
    public void flush() {
        if (this.original == null) this.logger.log(this.level, this.memory);
        else if (this.memory.startsWith("\tat")) {
            this.original.println(this.memory);
        } else if (this.memory.startsWith("Exception in thread \"")) {
            final String[] msg = this.memory.split(":", 2);
            if (msg.length == 2) this.logger.log(this.level, msg[1].strip());
            else this.logger.log(this.level, this.memory);
            this.original.println(this.memory);
        } else {
            this.logger.log(this.level, this.memory);
        }
        this.memory = "";
    }
}
