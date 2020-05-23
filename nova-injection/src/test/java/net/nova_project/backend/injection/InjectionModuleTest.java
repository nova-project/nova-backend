package net.nova_project.backend.injection;

import com.google.inject.Guice;
import net.nova_project.backend.injection.utils.InjectionTestClass;
import net.nova_project.backend.injection.utils.InjectionTestObject;
import net.nova_project.backend.injection.utils.InjectionTestObjectImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InjectionModuleTest {

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
    void configure() {
        assertEquals(
                Guice.createInjector(new InjectionModule(this.injectionBinders))
                        .getInstance(InjectionTestClass.class)
                        .getTestObject(),
                this.testObject
        );
    }
}
