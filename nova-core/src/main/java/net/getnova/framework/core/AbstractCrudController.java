package net.getnova.framework.core;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractCrudController<D, I> implements CrudController<D, I> {

  private final CrudService<D, I> service;

  @Override
  public Set<D> findAll() {
    return this.service.findAll();
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
