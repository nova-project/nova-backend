package net.getnova.backend.config;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is the config service which hales all configs, for other services, modules or the main config.
 * <b>The config service <i>MUST</i> be initialized before all other services.</b>
 */
@Slf4j
@Service("config")
@Singleton
public class ConfigService {

    private final Map<String, Set<ConfigValueData>> configs;
    private final File configFile;
    private boolean environment;

    /**
     * Creates a new {@link ConfigService}.
     */
    public ConfigService() {
        this.environment = false;
        this.configs = new HashMap<>();
        this.configFile = new File("config.yaml").getAbsoluteFile();
    }

    /**
     * The values are now read from the environment variables.
     */
    public void useEnvironment() {
        this.environment = true;
    }

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        if (this.environment) {
            log.info("Config values will be read from the environment variables.");
            ConfigUtils.loadFromEnvironment(this.configs);
        } else {
            try {
                if (!this.configFile.exists()) {
                    final File parentFile = this.configFile.getParentFile();
                    if (!parentFile.exists()) {
                        if (!parentFile.canWrite()) {
                            log.error("Missing permissions to create parent folder \"{}\" of the config file. Using default config values.", parentFile.getAbsolutePath());
                        } else parentFile.mkdirs();
                    }

                    if (!this.configFile.canWrite()) {
                        log.error("Missing permissions to create config file \"{}\". Using default config values.", this.configFile.getAbsolutePath());
                    } else if (this.configFile.createNewFile()) {
                        log.info("Created configuration file {}.", this.configFile.getName());
                    }
                }
                if (this.configFile.exists()) {
                    if (!this.configFile.canRead()) {
                        log.error("Missing permissions to read config file \"{}\". Using default config values.", this.configFile.getAbsolutePath());
                    } else ConfigUtils.loadFromFile(this.configFile, this.configs);

                    if (!this.configFile.canWrite()) {
                        log.error("Missing permissions to write config file \"{}\". The possibly still missing config values cannot be added.", this.configFile.getAbsolutePath());
                    } else ConfigUtils.save(this.configFile, this.configs);
                }
            } catch (IOException e) {
                log.error("Unable to create, read from or write to config file\"" + this.configFile.getAbsolutePath() + "\".", e);
            }
        }
    }

    /**
     * This method adds a new Config file to the config lifecycle.
     *
     * @param id     the id of the config file
     * @param config the instance of the config
     * @param <T>    the type of the config object
     * @return the instance of the config
     */
    public <T> T addConfig(final String id, final T config) {
        this.configs.put(id.strip(), ConfigUtils.parseConfig(config));
        return config;
    }
}
