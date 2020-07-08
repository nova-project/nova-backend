package net.getnova.backend;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;
import net.getnova.backend.logging.NovaLogLevel;

@Data
@Setter(AccessLevel.NONE)
public class NovaConfig {

    @ConfigValue(
            id = "log-level",
            comment = {"Available LogLevel's: OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE & ALL"}
    )
    private String loglevel = NovaLogLevel.WARN.toString();

    @ConfigValue(
            id = "debug",
            comment = {"If this is set to true, you get some more debug information's in the console."}
    )
    private boolean debug = false;
}
