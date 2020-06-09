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

    public ModuleHandler() {
        this.moduleFolder = new File("modules");
    }

    private void checkFolder() {
        if (!this.moduleFolder.exists()) this.moduleFolder.mkdirs();
    }

    public void loadModules() {
        this.checkFolder();
        try {
            this.modules = ModuleLoader.loadModules(this.moduleFolder);
        } catch (IOException e) {
            log.error("Unable to load modules.", e);
        }
    }

    public Set<Class<?>> getModuleServices() {
        return this.modules == null ? Collections.emptySet() : this.modules.stream().flatMap(module -> module.getServices().stream()).collect(Collectors.toUnmodifiableSet());
    }
}
