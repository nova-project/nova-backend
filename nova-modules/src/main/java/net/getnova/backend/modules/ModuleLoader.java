package net.getnova.backend.modules;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

final class ModuleLoader {

    private ModuleLoader() {
        throw new UnsupportedOperationException();
    }

    static Set<ModuleData> loadModules(final File moduleFolder) throws IOException {
        if (!moduleFolder.exists()) {
            throw new ModuleException("Module folder \"" + moduleFolder.getAbsolutePath() + "\" not found.");
        }

        final Set<ModuleData> modules = new LinkedHashSet<>();
        for (final File file : moduleFolder.listFiles()) modules.add(loadModule(file));
        return modules;
    }

    private static ModuleData loadModule(final File file) throws IOException {
        try (JarFile jarFile = new JarFile(file)) {
            final URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});

            final Manifest manifest = jarFile.getManifest();
            if (manifest == null) {
                throw new ModuleException("Missing manifest file in jar file \"" + jarFile.getName() + "\".");
            }

            final Attributes attributes = manifest.getMainAttributes();
            final String moduleName = loadValue(jarFile, attributes, "Module-Name");
            final String moduleVersion = loadValue(jarFile, attributes, "Module-Version");
            final String moduleServices = loadValue(jarFile, attributes, "Module-Services");

            final Set<Class<?>> services = new LinkedHashSet<>();
            for (final String serviceName : moduleServices.split(",")) {
                try {
                    services.add(urlClassLoader.loadClass(serviceName));
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
}
