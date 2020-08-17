package net.getnova.backend.modules;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

@Slf4j
final class ModuleLoader {

  private ModuleLoader() {
    throw new UnsupportedOperationException();
  }

  static Result loadModules(final File moduleFolder) throws IOException {
    if (!moduleFolder.exists()) {
      throw new ModuleException("Module folder \"" + moduleFolder.getAbsolutePath() + "\" not found.");
    }

    final File[] files = moduleFolder.listFiles();
    final URL[] urls = new URL[files.length];

    for (int i = 0; i < files.length; i++) urls[i] = files[i].toURI().toURL();
    final ClassLoader loader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

    return new Result(loader, Arrays.stream(files)
      .map(file -> {
        try {
          return loadModule(loader, file);
        } catch (IOException e) {
          log.error("Unable to load module.", e);
          return null;
        }
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toUnmodifiableSet()));
  }

  private static ModuleData loadModule(final ClassLoader loader, final File file) throws IOException {
    try (JarFile jarFile = new JarFile(file)) {

      final Manifest manifest = jarFile.getManifest();
      if (manifest == null) {
        throw new ModuleException("Missing manifest file in jar file \"" + jarFile.getName() + "\".");
      }

      final Attributes attributes = manifest.getMainAttributes();
      final String moduleName = loadValue(jarFile, attributes, "Module-Name");
      final String moduleVersion = loadValue(jarFile, attributes, "Module-Version");
      final String moduleServices = loadValue(jarFile, attributes, "Module-Services");

      final Set<Class<?>> services = new HashSet<>();
      for (final String serviceName : moduleServices.split(",")) {
        try {
          services.add(loader.loadClass(serviceName));
        } catch (ClassNotFoundException e) {
          throw new ModuleException("Module service class \"" + serviceName + "\" can not found in jar file \"" + jarFile.getName() + "\".");
        }
      }

      return new ModuleData(moduleName, moduleVersion, services);
    }
  }

  private static String loadValue(final JarFile jarFile, final Attributes attributes, final String name) throws ModuleException {
    final String value = attributes.getValue(name);
    if (value == null) {
      throw new ModuleException("Missing manifest attribute \"" + name + "\" in jar file \"" + jarFile.getName() + "\".");
    }
    return value;
  }

  @Data
  static final class Result {
    private final ClassLoader loader;
    private final Set<ModuleData> modules;
  }
}
