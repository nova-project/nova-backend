package net.getnova.backend.api.handler.rest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;

@Data
@Setter(AccessLevel.NONE)
public final class RestApiConfig {

    @ConfigValue(id = "path", comment = "The path of in the url, were the rest api is listening.")
    private String path = "api";
}
