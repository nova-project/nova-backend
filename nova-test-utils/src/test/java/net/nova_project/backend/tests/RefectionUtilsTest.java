package net.nova_project.backend.tests;

import net.nova_project.backend.tests.utils.ReflectionTestObject;
import net.nova_project.backend.tests.utils.ReflectionTestSecondObject;
import net.nova_project.backend.tests.utils.ReflectionTestThirdObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class RefectionUtilsTest {

    private String testString;
    private ReflectionTestObject testObject;

    @BeforeEach
    void setUp() {
        this.testString = "This is a string!";
        this.testObject = new ReflectionTestObject(this.testString);
    }

    @AfterEach
    void tearDown() {
        this.testString = null;
        this.testObject = null;
    }

    @Test
    void constructor() throws IllegalAccessException, InstantiationException {
        assertTrue(RefectionUtils.checkPrivateUnsupportedOperationConstructor(RefectionUtils.class));
    }

    @Test
    void newInstance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String firstString = "First";
        final String secondString = "Second";

        assertEquals(
                firstString + secondString,
                RefectionUtils.newInstance(ReflectionTestObject.class, new Class[]{String.class, String.class}, new String[]{firstString, secondString})
                        .getName()
        );
    }

    @Test
    void checkPrivateUnsupportedOperationConstructor() throws InstantiationException, IllegalAccessException {
        assertTrue(RefectionUtils.checkPrivateUnsupportedOperationConstructor(ReflectionTestSecondObject.class));
        assertFalse(RefectionUtils.checkPrivateUnsupportedOperationConstructor(ReflectionTestObject.class));
        assertFalse(RefectionUtils.checkPrivateUnsupportedOperationConstructor(ReflectionTestThirdObject.class));
    }

    @Test
    void getFieldValue() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(this.testString, RefectionUtils.getFieldValue(this.testObject, "name"));
    }

    @Test
    void steFieldValue() throws NoSuchFieldException, IllegalAccessException {
        final String secondTestString = "This is another string!";

        assertNotEquals(secondTestString, this.testObject.getName());
        RefectionUtils.steFieldValue(this.testObject, "name", secondTestString);
        assertEquals(secondTestString, this.testObject.getName());
    }
}
