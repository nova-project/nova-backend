/**
 * This module contains the api system.
 */
module nova.api {
  exports net.getnova.backend.api.annotations;
  exports net.getnova.backend.api.handler.rest;
  exports net.getnova.backend.api.handler.websocket;

  requires nova.json;
  requires nova.boot;
  requires nova.network;
  requires spring.beans;
  requires spring.context;
  requires org.reactivestreams;
  requires reactor.netty;
  requires com.google.gson;

  requires org.jetbrains.annotations;
  requires lombok;
}
