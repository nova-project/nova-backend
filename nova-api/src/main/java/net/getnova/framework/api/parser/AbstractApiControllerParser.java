package net.getnova.framework.api.parser;

import java.util.Optional;
import net.getnova.framework.api.data.ApiController;
import net.getnova.framework.api.data.ApiControllerMetadata;
import net.getnova.framework.core.ReflectionUtils;

public abstract class AbstractApiControllerParser implements ApiControllerParser {

  @Override
  public Optional<ApiController> parse(final Object object) {
    final Class<?> clazz = ReflectionUtils.getClass(object);
    return this.parse(clazz).map(metadata -> new ApiController(metadata.getPath(), clazz, object));
  }

  public abstract Optional<ApiControllerMetadata> parse(Class<?> clazz);
}
