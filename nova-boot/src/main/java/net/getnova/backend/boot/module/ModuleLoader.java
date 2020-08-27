package net.getnova.backend.boot.module;

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
public final class ModuleLoader {

  private ModuleLoader() {
    throw new UnsupportedOperationException();
  }

  static Result loadModules(final File moduleFolder) throws IOException {
    if (!moduleFolder.exists()) {
      throw new ModuleException(String.format("Module folder \"%s\" not found.", moduleFolder.getAbsolutePath()));
    }

    final File[] files = moduleFolder.listFiles();
    final URL[] urls = new URL[files.length];

    for (int i = 0; i < files.length; i++) urls[i] = files[i].toURI().toURL();
    final ClassLoader loader = URLClassLoader.newInstance(urls, ModuleLoader.class.getClassLoader());

    return new Result(loader, Arrays.stream(files)
      .map(file -> {
        try {
          return loadModule(loader, file);
        } catch (ModuleException e) {
          if (e.getCause() != null) log.error(e.getMessage(), e.getCause());
          else log.error(e.getMessage());
        } catch (IOException e) {
          log.error("Unable to load module.", e);
        }
        return null;
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toUnmodifiableSet()));
  }

  public static Set<Class<?>> loadModules(final Class<?>... classes) {
    final Set<Class<?>> configurations = new HashSet<>();
    for (final Class<?> clazz : classes) {
      if (!clazz.isAnnotationPresent(Module.class)) {
        log.error("The requested configuration class {} does not have the Annotation {}.", clazz.getName(), Module.class.getName());
        continue;
      }
      configurations.addAll(loadModules(clazz.getAnnotation(Module.class).value()));
      configurations.add(clazz);
    }
    return configurations;
  }

  private static ModuleData loadModule(final ClassLoader loader, final File file) throws IOException {
    try (JarFile jarFile = new JarFile(file)) {

      final Manifest manifest = jarFile.getManifest();
      if (manifest == null) {
        throw new ModuleException(String.format("Missing manifest in jar file \"%s\".", jarFile.getName()));
      }

      final Attributes attributes = manifest.getMainAttributes();
      final String moduleName = loadValue(jarFile, attributes, "Module-Name");
      final String moduleVersion = loadValue(jarFile, attributes, "Module-Version");
      final String moduleMainClassName = loadValue(jarFile, attributes, "Module-Main-Class");

      Class<?> mainClass;
      try {
        mainClass = Class.forName(moduleMainClassName, true, loader);
      } catch (ClassNotFoundException e) {
        throw new ModuleException(String.format("Module (\"%s\") main class \"%s\" can not found in jar file \"%s\".",
          moduleName, moduleMainClassName, jarFile.getName()));
      }

      if (!mainClass.isAnnotationPresent(Module.class)) {
        throw new ModuleException(String.format("Module (\"%s\") main class \"%s\" is missing annotation \"%s\".",
          moduleName, moduleMainClassName, Module.class.getName()));
      }

      return new ModuleData(moduleName, moduleVersion, mainClass);
    }
  }

  private static String loadValue(final JarFile jarFile, final Attributes attributes, final String name) throws ModuleException {
    final String value = attributes.getValue(name);
    if (value == null) {
      throw new ModuleException(String.format("Missing manifest attribute \"%s\" in jar file \"%s\".",
        name, jarFile.getName()));
    }
    return value;
  }

  @Data
  static final class Result {
    private final ClassLoader loader;
    private final Set<ModuleData> modules;
  }
}
