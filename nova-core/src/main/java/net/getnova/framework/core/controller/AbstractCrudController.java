package net.getnova.framework.core.controller;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.core.service.CrudService;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AbstractCrudController<D, I> implements CrudController<D, I> {

  protected final CrudService<D, I> service;

  @Override
  @Transactional(readOnly = true)
  public Set<D> findAll() {
    return this.service.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public D get(final I id) {
    return this.service.findById(id);
  }

  @Override
  @Transactional
  public D post(final D dto) {
    return this.service.save(dto);
  }

  @Override
  @Transactional
  public D put(final I id, final D dto) {
    return this.service.save(id, dto);
  }

  @Override
  @Transactional
  public void delete(final I id) {
    this.service.delete(id);
  }
}
