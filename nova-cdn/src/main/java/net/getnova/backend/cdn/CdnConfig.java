package net.getnova.backend.cdn;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
class CdnConfig {

  @Value("${CDN_DATA_DIR:data}")
  private String dataDir;
}
