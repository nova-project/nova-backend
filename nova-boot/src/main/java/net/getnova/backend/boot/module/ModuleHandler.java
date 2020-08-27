package net.getnova.backend.boot.module;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;
import net.getnova.backend.boot.context.ContextHandler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ModuleHandler {

  private final Bootstrap bootstrap;
  private final File moduleFolder;
  private final boolean loadModules;

  public ModuleHandler(final Bootstrap bootstrap, final File moduleFolder) {
    this.bootstrap = bootstrap;
    this.moduleFolder = moduleFolder;

    if (!moduleFolder.exists() && moduleFolder.mkdirs())
      log.info("Created module folder \"{}\".", this.moduleFolder.getPath());

    if (!moduleFolder.canRead()) {
      log.error("Missing read permissions for module folder. (\"{}\")", this.moduleFolder.getPath());
      this.loadModules = false;
      return;
    }

    final File[] files = moduleFolder.listFiles();
    if (files.length == 0) {
      log.info("Module folder (\"{}\") is empty, no modules were loaded.", this.moduleFolder.getPath());
      this.loadModules = false;
      return;
    }

    this.loadModules = true;
  }

  public void loadModules() {
    if (!this.loadModules) return;

    final ContextHandler contextHandler = this.bootstrap.getContextHandler();
    try {
      final ModuleLoader.Result result = ModuleLoader.loadModules(this.moduleFolder);
      Thread.currentThread().setContextClassLoader(result.getLoader());
      result.getModules().forEach(module -> contextHandler.register(ModuleLoader.loadModules(module.getMainClass()).toArray(new Class[0])));
    } catch (ModuleException e) {
      if (e.getCause() != null) log.error(e.getMessage(), e.getCause());
      else log.error(e.getMessage());
    } catch (IOException e) {
      log.error("Unable to load modules.", e);
    }
  }

  public void loadDebugModules(final String... modules) {
    this.bootstrap.getContextHandler().register(
      ModuleLoader.loadModules(
        Arrays.stream(modules)
          .map(clazz -> {
            try {
              return Class.forName(clazz);
            } catch (ClassNotFoundException e) {
              log.error("Unable to load debug module {}: {}", clazz, e.toString());
              return null;
            }
          })
          .filter(Objects::nonNull)
          .toArray(Class[]::new)
      ).toArray(new Class<?>[0])
    );
  }
}
