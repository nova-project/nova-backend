package net.getnova.backend.boot.module;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public final class ModuleService {

  private final Bootstrap bootstrap;
  private final File moduleFolder;

  public ModuleService(final Bootstrap bootstrap) {
    this.bootstrap = bootstrap;
    this.moduleFolder = new File("modules").getAbsoluteFile();
  }

  @PostConstruct
  public void postConstruct() {
    if (this.checkFolder()) try {
      final ModuleLoader.Result result = ModuleLoader.loadModules(this.moduleFolder);
      Thread.currentThread().setContextClassLoader(result.getLoader());
      result.getModules().forEach(module -> {
//        this.bootstrap.addConfiguration(module.getMainClass());
//        ModuleLoader.loadConfigurations(module.getServices()).forEach(this.bootstrap::addConfiguration);
      });
//      if (!result.getModules().isEmpty()) this.bootstrap.refresh();
    } catch (ModuleException e) {
      if (e.getCause() != null) log.error(e.getMessage(), e.getCause());
      else log.error(e.getMessage());
    } catch (IOException e) {
      log.error("Unable to load modules.", e);
    }
  }

  private boolean checkFolder() {
    if (!this.moduleFolder.exists()) {
      if (!moduleFolder.getParentFile().canWrite()) {
        log.error("Missing permissions to create modules folder \"{}\".", moduleFolder.getAbsolutePath());
        return false;
      }
      if (this.moduleFolder.mkdirs()) {
        log.info("Created module folder {}.", this.moduleFolder.getName());
      }
      return true;
    }
    if (!this.moduleFolder.getParentFile().canRead()) {
      log.error("Missing permissions to read modules folder \"{}\".", moduleFolder.getAbsolutePath());
      return false;
    }
    return true;
  }
}
