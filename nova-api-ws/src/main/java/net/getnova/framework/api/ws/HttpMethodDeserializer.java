package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;

public class HttpMethodDeserializer extends JsonDeserializer<HttpMethod> {

  @Override
  public HttpMethod deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
    return HttpMethod.valueOf(parser.readValueAs(String.class));
  }
}
