package net.getnova.framework.core.controller;

import java.util.Set;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CrudController<D, I> {

  @GetMapping
  Set<D> findAll();

  @GetMapping("{id}")
  D get(@PathVariable I id);

  @PostMapping
  D post(@RequestBody D dto);

  @PutMapping("{id}")
  D put(@PathVariable I id, @RequestBody D dto);

  @DeleteMapping("{id}")
  void delete(@PathVariable I id);
}
