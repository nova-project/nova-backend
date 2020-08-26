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

  public static Set<Class<?>> loadModules(final Class<?>[] classes) {
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
        throw new ModuleException("Missing manifest file in jar file \"" + jarFile.getName() + "\".");
      }

      final Attributes attributes = manifest.getMainAttributes();
      final String moduleName = loadValue(jarFile, attributes, "Module-Name");
      final String moduleVersion = loadValue(jarFile, attributes, "Module-Version");
      final String moduleMainClassName = loadValue(jarFile, attributes, "Module-Main-Class");

      Class<?> mainClass;
      try {
        mainClass = loader.loadClass(moduleName);
      } catch (ClassNotFoundException e) {
        throw new ModuleException("Module main class \"" + moduleMainClassName + "\" can not found in jar file \"" + jarFile.getName() + "\".");
      }

      if (!mainClass.isAnnotationPresent(Module.class)) {
        throw new ModuleException("Module " + moduleName + " main class " + moduleMainClassName + " is missing annotation " + Module.class.getName() + ".");
      }

      final Class<?>[] configurations = mainClass.getAnnotation(Module.class).value();
      for (Class<?> configuration : configurations)
        if (!configuration.isAnnotationPresent(Module.class))
          throw new ModuleException("Module " + moduleName + " depends on configuration witch is missing annotation " + Module.class.getName() + ".");

      return new ModuleData(moduleName, moduleVersion, mainClass, configurations);
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
