open module nova.api {
  requires static com.google.gson;
  requires static io.netty.codec.http;
  requires static io.netty.common;
  requires static org.reactivestreams;
  requires static org.slf4j;
  requires static reactor.core;
  requires static reactor.netty.http;
  requires static spring.context;
  requires lombok;
  requires nova.json;
  requires nova.boot;
  requires java.annotation;
  requires nova.network;

  exports net.getnova.framework.api;
  exports net.getnova.framework.api.annotations;
  exports net.getnova.framework.api.data;
  exports net.getnova.framework.api.exception;
  exports net.getnova.framework.api.executor;
  exports net.getnova.framework.api.handler.playground;
  exports net.getnova.framework.api.handler.rest;
  exports net.getnova.framework.api.handler.websocket;
  exports net.getnova.framework.api.parser;
}
