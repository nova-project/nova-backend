package net.getnova.framework.api.parser;

import io.netty.handler.codec.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.getnova.framework.api.data.ApiEndpointMetadata;
import net.getnova.framework.api.endpoint.DeleteEndpoint;
import net.getnova.framework.api.endpoint.GetEndpoint;
import net.getnova.framework.api.endpoint.PatchEndpoint;
import net.getnova.framework.api.endpoint.PostEndpoint;
import net.getnova.framework.api.endpoint.PutEndpoint;

interface ApiEndpointMetadataParser {

  Set<ApiEndpointMetadataParser> PARSERS = Set.of(
    new GetApiEndpointMetadataParser(),
    new PostApiEndpointMetadataParser(),
    new PutApiEndpointMetadataParser(),
    new PatchApiEndpointMetadataParser(),
    new DeleteApiEndpointMetadataParser()
  );

  Optional<ApiEndpointMetadata> parse(Method method);

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class GetApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(GetEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.GET, annotation.value()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class PostApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PostEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.POST, annotation.value()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class PutApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PutEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.PUT, annotation.value()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class PatchApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PatchEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.PATCH, annotation.value()));
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  final class DeleteApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(DeleteEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.DELETE, annotation.value()));
    }
  }
}
