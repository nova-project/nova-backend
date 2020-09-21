module nova.boot {
  exports net.getnova.backend.boot;
  exports net.getnova.backend.boot.config;
  exports net.getnova.backend.boot.module;

  requires org.slf4j;
  requires logback.core;
  requires logback.classic;

  requires lombok;
}
