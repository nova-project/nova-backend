package net.getnova.backend.api.handler.playground;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;

@Data
@Setter(AccessLevel.NONE)
public final class PlaygroundConfig {

    @ConfigValue(id = "enabled", comment = "Specifies whether the playground should be made available.")
    private boolean enabled = false;

    @ConfigValue(id = "path", comment = "The path of in the url, were the playground is available.")
    private String path = "playground";
}
