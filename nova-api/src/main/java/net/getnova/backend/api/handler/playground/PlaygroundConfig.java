package net.getnova.backend.api.handler.playground;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
public final class PlaygroundConfig {

  @Value("${PLAYGROUND_ENABLED:false}")
  private boolean enabled;

  @Value("${PLAYGROUND_PATH:playground}")
  private String path;
}
