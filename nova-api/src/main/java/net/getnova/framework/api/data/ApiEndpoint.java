package net.getnova.framework.api.data;

import io.netty.handler.codec.http.HttpMethod;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import lombok.Data;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.ToApiResponse;
import net.getnova.framework.core.Executable;
import reactor.core.publisher.Mono;

@Data
public class ApiEndpoint {

  private final HttpMethod method;
  private final ApiPath path;
  private final List<ApiParameter<?>> parameters;
  private final Executable executable;
  private boolean data = false;

  public Mono<ApiResponse> execute(final Object... args) throws InvocationTargetException, IllegalAccessException {
    final Object response = this.executable.execute(args);

    if (response == null) {
      throw new NullPointerException("ApiResponse can not be null");
    }

    if (response instanceof Mono) {
      return ((Mono<?>) response).map(this::toApiResponse);
    }

    return Mono.just(this.toApiResponse(response));
  }

  private ApiResponse toApiResponse(final Object object) {
    if (object instanceof ApiResponse) {
      return (ApiResponse) object;
    }
    else if (object instanceof ToApiResponse) {
      return ((ToApiResponse) object).toApiResponse();
    }

    return ApiResponse.of(object);
  }
}
