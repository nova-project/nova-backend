package net.getnova.framework.boot.module;

import lombok.Data;

@Data
class ModuleData {

  private final String id;
  private final String version;

  private final Class<?> mainClass;
}
