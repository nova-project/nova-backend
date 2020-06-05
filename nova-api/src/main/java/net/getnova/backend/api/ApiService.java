package net.getnova.backend.api;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.codec.http.server.HttpServerService;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Service(value = "api", depends = {HttpServerService.class})
@Singleton
public class ApiService {

    @Inject
    private HttpServerService httpServerService;

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        final GraphQLSchema schema = ApiParser.parse(User.class);
        this.httpServerService.addLocationProvider("graphql", new GraphiQLLocationProvider(GraphQL.newGraphQL(schema).build()));
    }

    @StartService
    private void start(final StartServiceEvent event) {
    }
}
