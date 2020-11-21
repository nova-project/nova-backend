package net.getnova.framework.jpa;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.framework.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
class JpaConfig {

  @Value("${SQL_SERVER_LOCATION:postgresql://localhost:5432}")
  private String location;

  @Value("${SQL_SERVER_DATABASE:nova}")
  private String database;

  @Value("${SQL_SERVER_USERNAME:nova}")
  private String username;

  @Value("${SQL_SERVER_PASSWORD:nova}")
  private String password;

  @Value("${SQL_SHOW_STATEMENTS:false}")
  private boolean showStatements;
}
