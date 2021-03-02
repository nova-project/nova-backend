package net.getnova.framework.api.parser;

import io.netty.handler.codec.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import net.getnova.framework.api.annotation.DeleteEndpoint;
import net.getnova.framework.api.annotation.GetEndpoint;
import net.getnova.framework.api.annotation.PatchEndpoint;
import net.getnova.framework.api.annotation.PostEndpoint;
import net.getnova.framework.api.annotation.PutEndpoint;
import net.getnova.framework.api.data.ApiEndpointMetadata;

interface ApiEndpointMetadataParser {

  Set<ApiEndpointMetadataParser> PARSERS = Set.of(
    new GetApiEndpointMetadataParser(),
    new PostApiEndpointMetadataParser(),
    new PutApiEndpointMetadataParser(),
    new PatchApiEndpointMetadataParser(),
    new DeleteApiEndpointMetadataParser()
  );

  Optional<ApiEndpointMetadata> parse(Method method);

  final class GetApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(GetEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.GET, annotation.value()));
    }
  }

  final class PostApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PostEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.POST, annotation.value()));
    }
  }

  final class PutApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PutEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.PUT, annotation.value()));
    }
  }

  final class PatchApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(PatchEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.PATCH, annotation.value()));
    }
  }

  final class DeleteApiEndpointMetadataParser implements ApiEndpointMetadataParser {

    @Override
    public Optional<ApiEndpointMetadata> parse(final Method method) {
      return Optional.ofNullable(method.getAnnotation(DeleteEndpoint.class))
        .map(annotation -> new ApiEndpointMetadata(HttpMethod.DELETE, annotation.value()));
    }
  }
}
