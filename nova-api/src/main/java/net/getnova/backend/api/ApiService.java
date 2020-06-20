package net.getnova.backend.api;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.generator.ApiMutationGenerator;
import net.getnova.backend.api.generator.ApiQueryGenerator;
import net.getnova.backend.api.generator.ApiSchemaGenerator;
import net.getnova.backend.api.generator.ApiTypeGenerator;
import net.getnova.backend.api.netty.ApiLocationProvider;
import net.getnova.backend.codec.http.server.HttpServerService;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PostInitService;
import net.getnova.backend.service.event.PostInitServiceEvent;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service(value = "api", depends = {HttpServerService.class})
@Singleton
public class ApiService {

    private final Set<Class<?>> types;
    private final Set<Object> queries;
    private final Set<Object> mutations;

    @Inject
    private HttpServerService httpServerService;

    @Inject
    private InjectionHandler injectionHandler;

    public ApiService() {
        this.types = new LinkedHashSet<>();
        this.queries = new LinkedHashSet<>();
        this.mutations = new LinkedHashSet<>();
    }

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        this.addType(User.class);

        final UserQuery userQuery = new UserQuery();
        this.addMutation(userQuery);
        this.addQuery(userQuery);
    }

    @PostInitService
    private void postInit(final PostInitServiceEvent event) {
        final GraphQLSchema schema = ApiSchemaGenerator.generateGraphQLSchema(
                ApiTypeGenerator.generateTypes(this.types),
                ApiQueryGenerator.generateQueries(this.queries),
                ApiMutationGenerator.generateMutations(this.mutations)
        );

        this.httpServerService.addLocationProvider("api", new ApiLocationProvider(GraphQL.newGraphQL(schema).build()));
    }

    public void addType(final Class<?> type) {
        this.types.add(type);
    }

    public <T> T addQuery(final T query) {
        this.queries.add(query);
        this.injectionHandler.getInjector().injectMembers(query);
        return query;
    }

    public <T> T addQuery(final Class<? extends T> query) {
        final T instance = this.injectionHandler.getInjector().getInstance(query);
        this.queries.add(instance);
        return instance;
    }

    public <T> T addMutation(final T mutation) {
        this.mutations.add(mutation);
        this.injectionHandler.getInjector().injectMembers(mutation);
        return mutation;
    }

    public <T> T addMutation(final Class<? extends T> mutation) {
        final T instance = this.injectionHandler.getInjector().getInstance(mutation);
        this.mutations.add(instance);
        return instance;
    }
}
