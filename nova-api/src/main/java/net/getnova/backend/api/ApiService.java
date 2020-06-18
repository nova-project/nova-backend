package net.getnova.backend.api;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.generator.ApiMutationGenerator;
import net.getnova.backend.api.generator.ApiQueryGenerator;
import net.getnova.backend.api.generator.ApiSchemaGenerator;
import net.getnova.backend.api.generator.ApiTypeGenerator;
import net.getnova.backend.api.netty.ApiLocationProvider;
import net.getnova.backend.codec.http.server.HttpServerService;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Slf4j
@Service(value = "api", depends = {HttpServerService.class})
@Singleton
public class ApiService {

    @Inject
    private HttpServerService httpServerService;

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {

        final UserQuery userQuery = new UserQuery();

        final GraphQLSchema schema = ApiSchemaGenerator.generateGraphQLSchema(
                ApiTypeGenerator.generateTypes(Set.of(User.class)),
                ApiQueryGenerator.generateQueries(Set.of(userQuery)),
                ApiMutationGenerator.generateMutations(Set.of(userQuery))
        );

        System.out.println(new SchemaPrinter().print(schema));

//        = ApiParser.parse(User.class);
        this.httpServerService.addLocationProvider("api", new ApiLocationProvider(GraphQL.newGraphQL(schema).build()));
    }

    @StartService
    private void start(final StartServiceEvent event) {
    }
}
