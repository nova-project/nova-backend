package net.getnova.framework.core;

import org.springframework.data.repository.CrudRepository;

public abstract class AbstractIdCrudService<D, I, M, P> extends AbstractCrudService<D, I, M, P> {

  private final String name;
  private final Converter<P, I> idConverter;

  public AbstractIdCrudService(
    final String name,
    final CrudRepository<M, P> repository,
    final Converter<M, D> converter,
    final Converter<P, I> idConverter
  ) {
    super(repository, converter);
    this.name = name;
    this.idConverter = idConverter;
  }

  @Override
  public boolean exist(final I id) {
    final P pId = this.idConverter.toModel(id);

    return this.repository.existsById(pId);
  }

  @Override
  public D save(final I id, final D dto) {
    final P pId = this.idConverter.toModel(id);

    final M model = this.repository.findById(pId)
      .orElseThrow(() -> new NotFoundException(this.name));

    this.converter.override(model, dto);

    return this.converter.toDto(model);
  }

  @Override
  public void delete(final I id) {
    final P pId = this.idConverter.toModel(id);

    if (!this.repository.existsById(pId)) {
      throw new NotFoundException(this.name);
    }

    this.repository.deleteById(pId);
  }
}
