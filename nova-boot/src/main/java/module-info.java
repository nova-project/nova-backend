open module nova.boot {
  requires java.base;
  requires java.logging;
  requires static logback.classic;
  requires static logback.core;
  requires static org.slf4j;
  requires static spring.context;
  requires static lombok;
  requires static jul.to.slf4j;
  requires static sentry;
  requires static sentry.logback;

  exports net.getnova.backend.boot;
  exports net.getnova.backend.boot.ansi;
  exports net.getnova.backend.boot.config;
  exports net.getnova.backend.boot.context;
  exports net.getnova.backend.boot.logging;
  exports net.getnova.backend.boot.logging.logback;
  exports net.getnova.backend.boot.module;
}
