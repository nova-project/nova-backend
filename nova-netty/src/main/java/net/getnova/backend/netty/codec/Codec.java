package net.getnova.backend.netty.codec;

public interface Codec {

  /**
   * Returns the {@link CodecInitializer} related to the current {@link Codec}.
   *
   * @return the {@link CodecInitializer} which is related to the current {@link Codec}
   */
  CodecInitializer getInitializer();
}
