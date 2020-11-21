open module nova.json {
  requires static com.google.gson;
  requires static org.slf4j;
  requires lombok;

  exports net.getnova.framework.json;
  exports net.getnova.framework.json.types;
}
