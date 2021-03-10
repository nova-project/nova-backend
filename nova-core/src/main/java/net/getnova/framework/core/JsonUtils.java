package net.getnova.framework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.ByteBufMono;

public final class JsonUtils {

  private JsonUtils() {
    throw new UnsupportedOperationException();
  }

  public static <T> Mono<T> readValue(
    final ObjectMapper mapper,
    final ByteBufMono byteBuf,
    final Class<T> clazz
  ) {
    return byteBuf.asByteArray()
      .map(data -> {
        try {
          return mapper.readValue(data, clazz);
        }
        catch (IOException e) {
          throw Exceptions.propagate(e);
        }
      });
  }

  public static <T> Mono<T> readValue(
    final ObjectMapper mapper,
    final ByteBufMono byteBuf,
    final Class<T> clazz,
    final Charset charset
  ) {
    return byteBuf.asString(charset)
      .map(data -> {
        try {
          return mapper.readValue(data, clazz);
        }
        catch (IOException e) {
          throw Exceptions.propagate(e);
        }
      });
  }

  public static <T> Flux<T> readValue(
    final ObjectMapper mapper,
    final ByteBufFlux byteBuf,
    final Class<T> clazz
  ) {
    return byteBuf.asByteArray()
      .map(data -> {
        try {
          return mapper.readValue(data, clazz);
        }
        catch (IOException e) {
          throw Exceptions.propagate(e);
        }
      });
  }

  public static <T> Flux<T> readValue(
    final ObjectMapper mapper,
    final ByteBufFlux byteBuf,
    final Class<T> clazz,
    final Charset charset
  ) {
    return byteBuf.asString(charset)
      .map(data -> {
        try {
          return mapper.readValue(data, clazz);
        }
        catch (IOException e) {
          throw Exceptions.propagate(e);
        }
      });
  }
}
