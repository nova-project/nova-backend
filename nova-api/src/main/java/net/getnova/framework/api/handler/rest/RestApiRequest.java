package net.getnova.framework.api.handler.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.getnova.framework.api.data.ApiRequest;
import reactor.netty.http.server.HttpServerRequest;

@Getter
@ToString
@EqualsAndHashCode
public class RestApiRequest extends ApiRequest {

  private final HttpServerRequest httpRequest;

  public RestApiRequest(final String endpoint, final JsonNode data, final HttpServerRequest httpRequest) {
    super(endpoint, data);
    this.httpRequest = httpRequest;
  }
}
