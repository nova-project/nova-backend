package net.getnova.backend.boot.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class ContextHandler {

  private final AnnotationConfigApplicationContext applicationContext;

  public <T> ContextHandler(final Class<T> clazz, final T instance, final Class<?>... beans) {
    this.applicationContext = new AnnotationConfigApplicationContext();
    this.applicationContext.registerBean(clazz, () -> instance);
    if (beans.length != 0) this.applicationContext.register(beans);
  }

  public void register(final Class<?>... classes) {
    if (classes.length != 0) this.applicationContext.register(classes);
  }

  public boolean refresh() {
    try {
      this.applicationContext.refresh();
      return true;
    } catch (Throwable cause) {
      log.error("Unable to bootstrap modules.", cause);
      return false;
    }
  }

  public void close() {
    this.applicationContext.close();
  }
}
