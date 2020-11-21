open module nova.data.jpa {
  requires static com.zaxxer.hikari;
  requires static java.persistence;
  requires static org.slf4j;
  requires static spring.context;
  requires static spring.orm;
  requires static spring.tx;
  requires lombok;
  requires nova.boot;
  requires spring.beans;
  requires java.sql;
  requires nova.json;

  exports net.getnova.framework.jpa;
  exports net.getnova.framework.jpa.model;
}
