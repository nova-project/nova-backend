module nova.network {
  exports net.getnova.backend.network.server;
  exports net.getnova.backend.network.server.http;

  requires nova.boot;
  requires spring.beans;

  requires lombok;
}
