package net.getnova.framework.core;

import org.springframework.aop.support.AopUtils;

public final class ReflectionUtils {

  private ReflectionUtils() {
    throw new UnsupportedOperationException();
  }

  public static Class<?> getClass(final Object object) {
    return AopUtils.getTargetClass(object);
  }
}
