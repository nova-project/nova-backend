package net.getnova.backend.codec.http.server;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;

@Data
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.NONE)
class HttpServerConfig {

    @ConfigValue(id = "host", comment = "The host of the http server.")
    private String host = "0.0.0.0";

    @ConfigValue(id = "port", comment = "The port of the server.")
    private int port = 80;
}
