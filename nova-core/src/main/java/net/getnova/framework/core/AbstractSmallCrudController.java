package net.getnova.framework.core;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractSmallCrudController<D, S, I> implements SmallCrudController<D, S, I> {

  private final SmallCrudService<D, S, I> service;

  @Override
  public Set<S> findAll() {
    return this.service.findAll();
  }

  @Override
  public D get(final I id) {
    return this.service.findById(id);
  }

  @Override
  public D post(final D dto) {
    return this.service.save(dto);
  }

  @Override
  public D put(final I id, final D dto) {
    return this.service.save(id, dto);
  }

  @Override
  public void delete(final I id) {
    this.service.delete(id);
  }
}
