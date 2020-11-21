package net.getnova.framework.cdn;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.framework.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
class CdnConfig {

  @Value("${CDN_DATA_DIR:data}")
  private String dataDir;
}
