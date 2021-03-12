package net.getnova.framework.api.parser;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import net.getnova.framework.api.ApiBootstrappingException;
import net.getnova.framework.api.data.ApiEndpoint;

@EqualsAndHashCode
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public final class ApiParser {

  private final ApiControllerParser controllerParser;
  private final ApiEndpointParser endpointParser;

  public ApiParser(
    final ApiControllerParser controllerParser,
    final Set<ApiParameterParser<?, ?>> parameterParsers
  ) {
    this.controllerParser = controllerParser;
    this.endpointParser = new ApiEndpointParser(parameterParsers);
  }

  public Set<ApiEndpoint> parse(final Collection<Object> objects) throws ApiBootstrappingException {
    return objects.stream()
      .flatMap(clazz -> this.controllerParser.parse(clazz).stream())
      .flatMap(this.endpointParser::parse)
      .collect(Collectors.toUnmodifiableSet());
  }
}
