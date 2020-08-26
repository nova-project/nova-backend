package net.getnova.backend.api.handler.rest;

import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Config
public final class RestApiConfig {

  @Value("${REST_API_PATH:api}")
  private String path;
}
