package net.getnova.framework.cdn;

import java.io.File;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.boot.module.Module;
import net.getnova.framework.cdn.data.CdnFileResolver;
import net.getnova.framework.jpa.JpaModule;
import net.getnova.framework.network.server.http.HttpServerModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Getter
@ComponentScan
@Module({HttpServerModule.class, JpaModule.class})
@EnableJpaRepositories
public class CdnModule {

  private final File dataDir;

  public CdnModule(final CdnConfig config, final CdnFileResolver resolver, final HttpServerModule httpServerModule) {
    this.dataDir = new File(config.getDataDir()).getAbsoluteFile();
    if (!this.dataDir.exists() && this.dataDir.mkdirs()) {
      log.info("Created cdn data dir \"{}\".", this.dataDir);
    }

    httpServerModule.getRoutes().addRoute("cdn", new CdnRoute(resolver));
  }
}
