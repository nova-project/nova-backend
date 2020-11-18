open module nova.json {
  requires static com.google.gson;
  requires static org.slf4j;
  requires lombok;

  exports net.getnova.backend.json;
  exports net.getnova.backend.json.types;
}
