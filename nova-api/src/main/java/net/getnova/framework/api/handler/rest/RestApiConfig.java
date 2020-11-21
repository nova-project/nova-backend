package net.getnova.framework.api.handler.rest;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.framework.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
final class RestApiConfig {

  @Value("${REST_API_PATH:api}")
  private String path;
}
