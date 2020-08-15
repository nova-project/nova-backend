package net.getnova.backend.injection;

import com.google.inject.Stage;
import net.getnova.backend.injection.utils.InjectionTestClass;
import net.getnova.backend.injection.utils.InjectionTestObject;
import net.getnova.backend.injection.utils.InjectionTestObjectImpl;
import net.getnova.backend.tests.RefectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InjectionHandlerTest {

  private Set<InjectionBinder> injectionBinders;
  private InjectionTestObject testObject;

  @BeforeEach
  void setUp() {
    this.injectionBinders = new HashSet<>();
    this.testObject = new InjectionTestObjectImpl();
    this.injectionBinders.add(binder -> binder.bind(InjectionTestObject.class).toInstance(this.testObject));
  }

  @AfterEach
  void tearDown() {
    this.injectionBinders = null;
  }

  @Test
  void addInjectionBinder() throws NoSuchFieldException, IllegalAccessException {
    final InjectionHandler injectionHandler = new InjectionHandler();
    this.injectionBinders.forEach(injectionHandler::addInjectionBinder);

    assertIterableEquals(
      this.injectionBinders,
      (Iterable<?>) RefectionUtils.getFieldValue(injectionHandler, "binders")
    );
  }

  @Test
  void createBindings() {
    final InjectionHandler injectionHandler = new InjectionHandler();
    this.injectionBinders.forEach(injectionHandler::addInjectionBinder);
    injectionHandler.createBindings(Stage.PRODUCTION);

    assertEquals(
      injectionHandler.getInjector()
        .getInstance(InjectionTestClass.class)
        .getTestObject(),
      this.testObject
    );
  }
}
