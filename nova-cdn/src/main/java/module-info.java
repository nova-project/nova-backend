open module nova.cdn {
  requires static io.netty.codec;
  requires static io.netty.codec.http;
  requires static java.persistence;
  requires static org.reactivestreams;
  requires static org.slf4j;
  requires static reactor.netty.http;
  requires static spring.context;
  requires static spring.core;
  requires static spring.data.jpa;
  requires nova.boot;
  requires lombok;
  requires spring.beans;
  requires nova.data.jpa;
  requires nova.network;

  exports net.getnova.framework.cdn;
  exports net.getnova.framework.cdn.data;
}
