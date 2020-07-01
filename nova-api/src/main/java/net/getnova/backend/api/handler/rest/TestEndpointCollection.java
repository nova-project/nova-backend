package net.getnova.backend.api.handler.rest;

import net.getnova.backend.api.annotations.ApiEndpoint;
import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;

@ApiEndpointCollection(id = "server", description = "Utils to control servers.")
public final class TestEndpointCollection {

    @ApiEndpoint(id = "list", description = "List all available servers.")
    private ApiResponse list() {
        return new ApiResponse(ApiResponseStatus.OK);
    }
}
