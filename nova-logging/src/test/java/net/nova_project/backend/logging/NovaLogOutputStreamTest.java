package net.nova_project.backend.logging;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NovaLogOutputStreamTest {

    @Test
    void write() throws IOException, NoSuchFieldException, IllegalAccessException {
        final NovaLogOutputStream logOutputStream = new NovaLogOutputStream("test", NovaLogLevel.INFO, null);

        final String message = "This is a unit test!";
        final byte[] content = message.getBytes(StandardCharsets.UTF_8);

        logOutputStream.write(content);
        assertEquals(message, this.getMemory(logOutputStream));

        logOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        assertEquals("", this.getMemory(logOutputStream));
    }

//    @Test
//    void flush() {
//    }

    private String getMemory(final NovaLogOutputStream logOutputStream) throws NoSuchFieldException, IllegalAccessException {
        final Field memoryField = NovaLogOutputStream.class.getDeclaredField("memory");
        memoryField.setAccessible(true);
        final String memory = (String) memoryField.get(logOutputStream);
        memoryField.setAccessible(false);
        return memory;
    }
}
