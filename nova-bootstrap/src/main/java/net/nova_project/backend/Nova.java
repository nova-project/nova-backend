package net.nova_project.backend;

import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.logging.NovaLogConfigurer;

@Slf4j
public class Nova {

    /**
     * This is the "main" entry point of the nova backend.
     *
     * @param args the program arguments
     * @see Bootstrap#main(String[])
     */
    public Nova(final String[] args) {
        NovaLogConfigurer.redirectSysLog();
        log.info("Hello World!");
    }
}
