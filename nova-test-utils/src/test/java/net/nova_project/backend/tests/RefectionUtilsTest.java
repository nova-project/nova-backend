package net.nova_project.backend.tests;

import net.nova_project.backend.tests.utils.ReflectionTestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testConstructor() throws IllegalAccessException, InstantiationException {
        assertTrue(RefectionUtils.checkPrivateUnsupportedOperationConstructor(RefectionUtils.class));
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
