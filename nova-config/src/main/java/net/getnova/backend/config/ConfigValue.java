package net.getnova.backend.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {

  /**
   * This field contains the id of the config value.
   *
   * @return the id of the value
   */
  String id();

  /**
   * This field contains the comment of the config value.
   *
   * @return the comment of the config value
   */
  String[] comment();
}
