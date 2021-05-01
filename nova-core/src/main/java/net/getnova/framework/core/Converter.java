package net.getnova.framework.core;

public interface Converter<I, M, D> {

  M toModel(D dto);

  M toModel(I id, D dto);

  D toDto(M model);
}
