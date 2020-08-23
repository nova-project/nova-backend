package net.getnova.backend.boot;

import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Config
public final class BootConfig {

  @Value("${DEBUG:false}")
  private boolean debug;

  @Value("${LOG_LEVEL:WARN}")
  private String level;
}
