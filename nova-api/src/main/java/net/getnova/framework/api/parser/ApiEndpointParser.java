package net.getnova.framework.api.parser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.ApiController;
import net.getnova.framework.api.data.ApiEndpoint;
import net.getnova.framework.api.data.ApiEndpointMetadata;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.ApiPath;
import net.getnova.framework.core.Executable;

@RequiredArgsConstructor
final class ApiEndpointParser {

  private final Set<ApiParameterParser<?, ?>> parameterParsers;

  Stream<ApiEndpoint> parse(final ApiController controller) {
    return Arrays.stream(controller.getMethods())
      .flatMap(method -> this.parse(controller, method).stream());
  }

  private Optional<ApiEndpoint> parse(final ApiController controller, final Method method) {
    final Optional<ApiEndpointMetadata> optionalMetadata = this.parseMetadata(method);
    if (optionalMetadata.isEmpty()) {
      return Optional.empty();
    }

    final List<ApiParameter<?>> parameters = this.parseParameters(method);
    final ApiEndpointMetadata metadata = optionalMetadata.get();

    final String path = metadata.getPath().equals("/")
      ? controller.getPath()
      : controller.getPath() + metadata.getPath();

    return Optional.of(new ApiEndpoint(
      metadata.getMethod(),
      ApiPath.of(path),
      parameters,
      new Executable(controller.getObject(), method, false)
    ));
  }

  private Optional<ApiEndpointMetadata> parseMetadata(final Method method) {
    return ApiEndpointMetadataParser.PARSERS.stream()
      .flatMap(parser -> parser.parse(method).stream())
      .findFirst();
  }

  private List<ApiParameter<?>> parseParameters(final Method method) {
    return Arrays.stream(method.getParameters())
      .map(parameter -> this.parameterParsers.stream()
        .flatMap(parser -> parser.parse(parameter).stream())
        .findFirst() // TODO: custom exception, error message
        .orElseThrow(() -> new IllegalArgumentException("Unable to parse parameter")))
      .collect(Collectors.toList());
  }
}
