package net.getnova.backend.config;

import lombok.Data;

import java.lang.reflect.Field;

@Data
class ConfigValueData {

  private final String id;
  private final String[] comment;

  private final Field field;
  private final Object config;
}
