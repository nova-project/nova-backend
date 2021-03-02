package net.getnova.framework.api.data;

import java.lang.reflect.Method;
import lombok.Data;
import net.getnova.framework.core.PathUtils;

@Data
public class ApiController {

  private final String path;
  private final Class<?> clazz;
  private final Object object;

  public ApiController(final String path, final Class<?> clazz, final Object object) {
    this.path = PathUtils.normalizePath(path);
    this.clazz = clazz;
    this.object = object;
  }

  public Method[] getMethods() {
    return this.clazz.getMethods();
  }
}
