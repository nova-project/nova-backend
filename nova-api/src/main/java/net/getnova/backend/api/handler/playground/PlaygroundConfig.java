package net.getnova.backend.api.handler.playground;

import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Config
public final class PlaygroundConfig {

  @Value("${PLAYGROUND_ENABELD:false}")
  private boolean enabled;

  @Value("${PLAYGROUND_PATH:playground}")
  private String path;
}
