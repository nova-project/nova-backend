package net.getnova.backend.boot.module;

import lombok.Data;

@Data
public class ModuleData {

  private final String id;
  private final String version;

  private final Class<?> mainClass;
}
