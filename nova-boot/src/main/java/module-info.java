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

  exports net.getnova.framework.boot;
  exports net.getnova.framework.boot.ansi;
  exports net.getnova.framework.boot.config;
  exports net.getnova.framework.boot.context;
  exports net.getnova.framework.boot.logging;
  exports net.getnova.framework.boot.logging.logback;
  exports net.getnova.framework.boot.module;
}
