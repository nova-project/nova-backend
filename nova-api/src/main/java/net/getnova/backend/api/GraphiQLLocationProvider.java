package net.getnova.backend.api;

import graphql.GraphQL;
import lombok.Data;
import net.getnova.backend.codec.http.server.HttpLocationProvider;

@Data
public class GraphiQLLocationProvider implements HttpLocationProvider<GraphiQLLocation> {

    private final GraphQL graphQL;

    @Override
    public GraphiQLLocation getLocation() {
        return new GraphiQLLocation(this.graphQL);
    }
}
