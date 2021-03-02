package net.getnova.framework.api.data;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.getnova.framework.api.data.response.ApiResponse;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ApiMethodNotAllowedResponse extends ApiResponse {

  private final Collection<HttpMethod> methods;

  public ApiMethodNotAllowedResponse(final Collection<HttpMethod> methods) {
    super(HttpResponseStatus.METHOD_NOT_ALLOWED);
    this.methods = methods;
  }
}
