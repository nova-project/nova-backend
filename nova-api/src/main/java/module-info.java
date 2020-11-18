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

  exports net.getnova.backend.api;
  exports net.getnova.backend.api.annotations;
  exports net.getnova.backend.api.data;
  exports net.getnova.backend.api.exception;
  exports net.getnova.backend.api.executor;
  exports net.getnova.backend.api.handler.playground;
  exports net.getnova.backend.api.handler.rest;
  exports net.getnova.backend.api.handler.websocket;
  exports net.getnova.backend.api.parser;
}
