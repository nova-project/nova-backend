package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.IOException;

public class HttpResponseStatusSerializer extends JsonSerializer<HttpResponseStatus> {

  @Override
  public void serialize(final HttpResponseStatus value, final JsonGenerator gen, final SerializerProvider serializers)
    throws IOException {

    gen.writeStartObject();
    gen.writeNumberField("code", value.code());
    gen.writeStringField("name", value.reasonPhrase());
    gen.writeEndObject();
  }
}
