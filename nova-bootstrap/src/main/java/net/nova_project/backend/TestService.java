package net.nova_project.backend;

import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.service.Service;
import net.nova_project.backend.service.event.InitService;
import net.nova_project.backend.service.event.InitServiceEvent;
import net.nova_project.backend.service.event.PostInitService;
import net.nova_project.backend.service.event.PostInitServiceEvent;
import net.nova_project.backend.service.event.PreInitService;
import net.nova_project.backend.service.event.PreInitServiceEvent;
import net.nova_project.backend.service.event.StartService;
import net.nova_project.backend.service.event.StartServiceEvent;
import net.nova_project.backend.service.event.StopService;
import net.nova_project.backend.service.event.StopServiceEvent;

@Service("test")
@Slf4j
public class TestService {

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        log.info("TestService says something...");
    }

    @InitService
    private void init(final InitServiceEvent event) {
        log.info("TestService says something...");
    }

    @PostInitService
    private void postInit(final PostInitServiceEvent event) {
        log.info("TestService says something...");
    }

    @StartService
    private void start(final StartServiceEvent event) {
        log.info("TestService says something...");
    }

    @StopService
    private void stop(final StopServiceEvent event) {
        log.info("TestService says something...");
    }
}
