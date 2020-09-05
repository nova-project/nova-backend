package net.getnova.backend.api.handler.rest;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
public final class RestApiConfig {

  @Value("${REST_API_PATH:api}")
  private String path;
}
