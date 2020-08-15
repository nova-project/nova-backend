package net.getnova.backend.modules;

import lombok.Data;

import java.util.Set;

@Data
public class ModuleData {

  private final String id;
  private final String version;

  private final Set<Class<?>> services;
}
