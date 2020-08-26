package net.getnova.backend.boot.module;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class ModuleHandler {

  private final Bootstrap bootstrap;
  private final File moduleFolder;

  public ModuleHandler(final Bootstrap bootstrap, final File moduleFolder) {
    this.bootstrap = bootstrap;
    this.moduleFolder = moduleFolder;

    if (!moduleFolder.exists() && moduleFolder.mkdirs())
      log.info("Created module folder \"{}\".", this.moduleFolder.getPath());

    if (!moduleFolder.canRead()) {
      log.error("Missing read permissions for module folder. (\"{}\")", this.moduleFolder.getPath());
      return;
    }

    final File[] files = moduleFolder.listFiles();
    if (files.length == 0) {
      log.info("Module folder (\"{}\") is empty, no modules were loaded.", this.moduleFolder.getPath());
      return;
    }

    // TODO: load modules logic
  }

  public void loadModules() {
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
