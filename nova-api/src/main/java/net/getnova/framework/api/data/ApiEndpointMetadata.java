package net.getnova.framework.api.data;

import io.netty.handler.codec.http.HttpMethod;
import lombok.Data;
import net.getnova.framework.core.PathUtils;

@Data
public class ApiEndpointMetadata {

  private final HttpMethod method;
  private final String path;

  public ApiEndpointMetadata(final HttpMethod method, final String path) {
    this.method = method;
    this.path = PathUtils.normalizePath(path);
  }
}
