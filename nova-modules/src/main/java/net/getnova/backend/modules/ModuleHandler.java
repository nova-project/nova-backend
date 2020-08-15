package net.getnova.backend.modules;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode
public class ModuleHandler {

  private final File moduleFolder;

  @Getter
  private Set<ModuleData> modules;

  /**
   * Creates a new module handler.
   */
  public ModuleHandler() {
    this.moduleFolder = new File("modules").getAbsoluteFile();
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

  /**
   * Loads all modules.
   */
  public void loadModules() {
    if (this.checkFolder()) try {
      final ModuleLoader.Result result = ModuleLoader.loadModules(this.moduleFolder);
      Thread.currentThread().setContextClassLoader(result.getLoader());
      this.modules = result.getModules();
    } catch (IOException e) {
      log.error("Unable to load modules.", e);
    }
  }

  /**
   * Returns all services witch are needed by all registered modules.
   *
   * @return all services witch are needed by all registered modules
   */
  public Set<Class<?>> getModuleServices() {
    return this.modules == null ? Collections.emptySet() : this.modules.stream().flatMap(module -> module.getServices().stream()).collect(Collectors.toUnmodifiableSet());
  }
}
