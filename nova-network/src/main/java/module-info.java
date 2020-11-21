open module nova.network {
  requires static io.netty.codec.http;
  requires static org.reactivestreams;
  requires static org.slf4j;
  requires static reactor.core;
  requires static reactor.netty.core;
  requires static reactor.netty.http;
  requires static spring.context;
  requires static spring.core;
  requires lombok;
  requires nova.boot;
  requires spring.beans;
  requires io.netty.handler;
  requires java.annotation;
  requires io.netty.transport;

  exports net.getnova.framework.network.server;
  exports net.getnova.framework.network.server.http;
  exports net.getnova.framework.network.server.http.route;
}
