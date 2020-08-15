package net.getnova.backend.netty.codec;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
public class CodecHandler {

  private final List<CodecInitializer> initializers;

  /**
   * Adds some codecs to this {@link CodecHandler} witch
   * is able to provide all {@link CodecInitializer} in
   * the right order for a server.
   *
   * @param codecs the codecs which should be configured
   */
  public CodecHandler(final Codec... codecs) {
    this.initializers = Arrays.stream(codecs)
      .map(Codec::getInitializer)
      .collect(Collectors.toUnmodifiableList());
  }
}
