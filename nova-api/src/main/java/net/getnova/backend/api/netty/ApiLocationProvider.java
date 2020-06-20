package net.getnova.backend.api.netty;

import graphql.GraphQL;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.codec.http.server.HttpLocationProvider;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class ApiLocationProvider implements HttpLocationProvider<ApiLocation> {

    private final GraphQL graphQL;

    @Override
    public ApiLocation getLocation() {
        return new ApiLocation(this.graphQL);
    }
}
