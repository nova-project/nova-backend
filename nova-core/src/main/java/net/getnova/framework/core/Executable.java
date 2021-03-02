package net.getnova.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Data;

@Data
public final class Executable {

  private final Object object;
  private final Method method;

  public Executable(final Object object, final Method method, final boolean forceAccess) {
    this.object = object;
    this.method = method;

    if (forceAccess && !method.canAccess(object)) {
      method.setAccessible(true);
    }
  }

  public Object execute(final Object... args) throws InvocationTargetException, IllegalAccessException {
    return this.method.invoke(this.object, args);
  }

  @Override
  public String toString() {
    return String.format("{%s}", this.method.toString());
  }
}
