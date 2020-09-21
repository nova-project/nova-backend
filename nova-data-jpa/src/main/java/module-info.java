module data.jpa {
  exports net.getnova.backend.jpa;
  exports net.getnova.backend.jpa.model;

  requires nova.boot;
  requires nova.json;
  requires com.zaxxer.hikari;
  requires java.persistence;
  requires java.sql;
  requires spring.beans;
  requires spring.context;
  requires spring.orm;
  requires spring.tx;

  requires lombok;
}
