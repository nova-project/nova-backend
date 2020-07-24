import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class Test {

    private Method publicSetterMethod;
    private BiConsumer<TestObject, String> publicSetterHandle;

    @BeforeEach
    public void setUp() throws Throwable {
        publicSetterMethod = TestObject.class.getDeclaredMethod("setValue", String.class);

        publicSetterHandle = LambdaTools.createSetter(TestObject.class, "value", String.class);
    }

    @org.junit.jupiter.api.Test
    public void method_testSetterPerformance() throws Exception {
        TestObject testObject = new TestObject("TheValue");

        long value = 0;
        int amount = 0;
        for (int i = 0; i < 200; i++) {
            long timestamp = System.nanoTime();
            publicSetterMethod.invoke(testObject, "NewValue");
            long time = System.nanoTime() - timestamp;
            value += time;
            amount++;
        }

        System.out.println("Setter reflection average: " + (double) value / amount + "ns");
    }

    @org.junit.jupiter.api.Test
    public void function_testSetterPerformance() throws Throwable {
        TestObject testObject = new TestObject("TheValue");

        long value = 0;
        int amount = 0;
        for (int i = 0; i < 200; i++) {
            long timestamp = System.nanoTime();
            publicSetterHandle.accept(testObject, "NewValue");
            long time = System.nanoTime() - timestamp;
            value += time;
            amount++;
        }

        System.out.println("Setter lambda average: " + (double) value / amount + "ns");
    }
}
